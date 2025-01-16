package com.nexfly.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nexfly.ai.common.advisor.NexflyPromptChatMemoryAdvisor;
import com.nexfly.ai.common.function.FunctionManager;
import com.nexfly.ai.common.vectorstore.VectorStoreManager;
import com.nexfly.api.system.bean.AppEditResponse;
import com.nexfly.api.system.bean.AppModelInfo;
import com.nexfly.api.system.bean.AppSaveRequest;
import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.common.core.utils.UuidUtil;
import com.nexfly.oss.common.OssFileManager;
import com.nexfly.system.manager.DefaultModelManager;
import com.nexfly.system.manager.ModelManager;
import com.nexfly.system.mapper.*;
import com.nexfly.system.memory.NexflyChatMemory;
import com.nexfly.system.model.*;
import com.nexfly.system.service.AppService;
import com.nexfly.system.service.DatasetService;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.Media;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.*;

import static com.nexfly.common.core.constants.NexflyConstants.*;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private ModelManager modelManager;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private AppModelMapper appModelMapper;

    @Autowired
    private AppConfigMapper appConfigMapper;

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private VectorStoreManager vectorStoreManager;

    @Autowired
    private FunctionManager functionManager;

    @Autowired
    private AppConversationMapper appConversationMapper;

    @Autowired
    private AppMessageMapper appMessageMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ProviderModelMapper providerModelMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private OssFileManager ossFileManager;

    @Override
    public Flux<ChatResponse> chat(@NotNull NexflyMessage message) throws Exception {

        AppConversation appConversation = appConversationMapper.findById(message.conversationId());

        App app = appMapper.findById(appConversation.getAppId());

        // 查询app关联的大语言模型
        ChatModel chatModel = modelManager.getChatModel(app.getAppId());

        // 查找 app配置信息
        AppConfig appConfig = appConfigMapper.findByAppId(app.getAppId());
        // 系统消息提示词
        String system = appConfig.getPrePrompt();
        // 变量
//        Map<String, Object> formVariable = getFormVariable(appConfig.getFormVariable());
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(system);
        Message systemMessage = systemPromptTemplate.createMessage(); //formVariable.isEmpty() ? systemPromptTemplate.createMessage() : systemPromptTemplate.createMessage(formVariable);

        List<Message> messageList = new ArrayList<>();
        message.messages().forEach(m -> {
            if (StringUtils.isNotBlank(m.content())) {
                UserMessage userMessage = new UserMessage(m.content());
                if (m.documentIds() != null) {
                    userMessage.getMetadata().put(DOCUMENT_IDS, m.documentIds());
                }
                messageList.add(userMessage);
            }
        });

        List<Media> mediaList = new ArrayList<>();
        final Message userMessage = messageList.get(messageList.size() - 1);
        Object obj = userMessage.getMetadata().get(DOCUMENT_IDS);
        if (obj != null) {
            Long[] documentIds = (Long[]) obj;
            List<Long> documentIdList = Arrays.stream(documentIds).toList();
            if (!documentIdList.isEmpty()) {
                List<Attachment> attachmentList = attachmentMapper.getAttachmentListByIds(documentIdList);
                for (Attachment attachment : attachmentList) {
                    try (InputStream inputStream = ossFileManager.download(attachment.getPath())) {
                        Media media = new Media(MimeTypeUtils.IMAGE_PNG, new InputStreamResource(inputStream));
                        mediaList.add(media);
                    }
                }
            }
        }

        // 查找app关联的数据集配置信息
        List<Dataset> datasetList = datasetMapper.findDatasetListByAppId(app.getAppId());
        // 根据dataset构建List<RequestResponseAdvisor>
        List<Advisor> requestResponseAdvisorList = new ArrayList<>();
//        requestResponseAdvisorList.add(new SimpleLoggerAdvisor());
        requestResponseAdvisorList.add(new NexflyPromptChatMemoryAdvisor(new NexflyChatMemory(AuthUtils.getUserId(), app.getAppId(), appMessageMapper)));
        for (Dataset dataset : datasetList) {
            EmbeddingModel embeddingModel = modelManager.getEmbeddingModel(dataset.getDatasetId());
            var qaAdvisor = new QuestionAnswerAdvisor(vectorStoreManager.getVectorStoreFactory().getVectorStore(SEGMENT_INDEX.concat(String.valueOf(dataset.getOrgId())), embeddingModel),
                    SearchRequest.builder().similarityThreshold(appConfig.getSimilarityThreshold()).topK(appConfig.getTopN())
//                            .filterExpression("datasetId == " + dataset.getDatasetId())
                            .build()
                            );
            requestResponseAdvisorList.add(qaAdvisor);
        }

        return ChatClient.builder(chatModel)
                .build()
                .prompt()
                .system(systemMessage.getContent())
                .messages(messageList)
                .user(r -> r.text(userMessage.getContent())
                        .media(mediaList.toArray(new Media[0]))
                )
                .functions(functionManager.getFunctionList().stream().map(function -> FunctionCallbackWrapper.builder(function.function()).withName(function.name()).withDescription(function.description()).build()).toArray(FunctionCallback[]::new))
                .functions(functionManager.getFunctionList().stream().map(functionInfo -> firstLetterToLowerCase(functionInfo.function().getClass().getSimpleName())).toArray(String[]::new))
                .advisors(a -> a.param(USER_ID, AuthUtils.getUserId()).param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, message.conversationId()))
                .advisors(requestResponseAdvisorList)
                .stream()
                .chatResponse().map(r -> {
                    if (r.getResult() == null || r.getResult().getOutput() == null
                            || r.getResult().getOutput().getContent() == null) {
                        return new ChatResponse("true");
                    }
                    return new ChatResponse(new ChatResponseData(r.getResult().getOutput().getContent(), r.getMetadata(), message.conversationId() + UuidUtil.getSimpleUuid()));
                })
                .onErrorResume(e -> Mono.just(new ChatResponse("Error occurred: " + e.getMessage())))
                ;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(AppSaveRequest appParam) throws NexflyException {
        Long orgId = accountMapper.getUserOrg(AuthUtils.getUserId()).getOrgId();
        App app = appMapper.findById(appParam.getAppId());
        if (app == null) {
            app = new App();
        }
        app.setName(appParam.getName());
        app.setDescription(appParam.getDescription());
        app.setIcon(appParam.getIcon());
        app.setOrgId(orgId);
        saveOrUpdate(app);

        // 设置app配置
        AppSaveRequest.PromptConfig promptConfig = appParam.getPromptConfig();
        AppConfig appConfig = appConfigMapper.findByAppId(app.getAppId());
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.setAppId(app.getAppId());
            appConfig.setOrgId(orgId);
        }
        appConfig.setPrePrompt(promptConfig.getSystem());
        if (!promptConfig.getParameters().isEmpty()) {
            appConfig.setFormVariable(JSONArray.toJSONString(promptConfig.getParameters()));
        }
        appConfig.setEmptyResponse(promptConfig.getEmptyResponse());
        appConfig.setPrologue(promptConfig.getPrologue());
        appConfig.setQuote(promptConfig.getQuote());
        appConfig.setSimilarityThreshold(appParam.getSimilarityThreshold());
        appConfig.setTopN(appParam.getTopN());
        appConfig.setVectorSimilarityWeight(appParam.getVectorSimilarityWeight());

        saveOrUpdateAppConfig(appConfig);

        // 设置大模型model 以及 model 配置
        List<AppModelInfo> appModelList = appModelMapper.findListByAppId(app.getAppId());
        if (StringUtils.isNotBlank(appParam.getModelId())) {
            saveOrUpdateAppModel(app, getSelectModel(appParam.getModelId()), appModelList, appParam, DefaultModelManager.ModelType.CHAT);
        }
        if (StringUtils.isNotBlank(appParam.getRerankModelId())) {
            saveOrUpdateAppModel(app, getSelectModel(appParam.getRerankModelId()), appModelList, appParam, DefaultModelManager.ModelType.RERANK);
        }
        if (StringUtils.isNotBlank(appParam.getTtsModelId())) {
            saveOrUpdateAppModel(app, getSelectModel(appParam.getTtsModelId()), appModelList, appParam, DefaultModelManager.ModelType.TTS);
        }

        // 设置知识库配置
        if (!appParam.getDatasetIds().isEmpty()) {
            datasetMapper.deleteAppDataset(appParam.getAppId());
            datasetMapper.insertAppDatasetList(appParam.getDatasetIds().stream().map(item -> new DatasetService.AppDataset(appParam.getAppId(), item)).toList());
        }

        return true;
    }

    private String firstLetterToLowerCase(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    private void saveOrUpdateAppConfig(AppConfig appConfig) {
        if (appConfig.getConfigId() == null) {
            appConfigMapper.save(appConfig);
        } else {
            appConfigMapper.update(appConfig);
        }
    }

    private SelectModel getSelectModel(String selectModelId) {
        String[] modelInfoArray = selectModelId.split("@");
        String provider = modelInfoArray[0];
        String modelName = modelInfoArray[1];
        String modelType = modelInfoArray[2];
        return new SelectModel(provider, modelName, modelType);
    }

    record SelectModel(String provider, String modelName, String type) {

    }

    void saveOrUpdateAppModel(App app, SelectModel selectModel, List<AppModelInfo> appModelList, AppSaveRequest appParam, DefaultModelManager.ModelType modelType) {

        AppModelInfo appModelInfo = appModelList.stream().filter(m -> m.getModelType().equals(modelType.getValue())).findFirst().orElse(null);

        AppModel appModel;
        if (appModelInfo == null) {
            appModel = new AppModel();
            appModel.setAppId(app.getAppId());
        } else {
            appModel = appModelMapper.findById(appModelInfo.getAppModelId());
        }
        ProviderModel providerModel = providerModelMapper.getProviderModelByOrgAndName(app.getOrgId(), selectModel.provider, selectModel.modelName);
        if (providerModel == null) {
            providerModel = new ProviderModel();
            providerModel.setOrgId(app.getOrgId());
            providerModel.setProviderName(selectModel.provider);
            providerModel.setModelName(selectModel.modelName);
            providerModel.setModelType(selectModel.type);
            providerModelMapper.save(providerModel);
        }
        appModel.setModelId(providerModel.getModelId());
        appModel.setModelConfig(JSON.toJSONString(appParam.getModelConfig()));
        if (appModel.getAppModelId() == null) {
            appModelMapper.save(appModel);
        } else {
            appModelMapper.update(appModel);
        }
    }

    @Override
    public AppEditResponse findById(Long appId) {
        App app = appMapper.findById(appId);
        AppEditResponse appEditResponse = new AppEditResponse();
        appEditResponse.setAppId(app.getAppId());
        appEditResponse.setOrgId(app.getOrgId());
        appEditResponse.setName(app.getName());
        appEditResponse.setDescription(app.getDescription());
        appEditResponse.setIcon(app.getIcon());
        AppConfig appConfig = appConfigMapper.findByAppId(app.getAppId());
        appEditResponse.setSimilarityThreshold(appConfig.getSimilarityThreshold());
        appEditResponse.setVectorSimilarityWeight(appConfig.getVectorSimilarityWeight());
        appEditResponse.setTopN(appConfig.getTopN());
        AppSaveRequest.PromptConfig promptConfig = new AppSaveRequest.PromptConfig();
        promptConfig.setSystem(appConfig.getPrePrompt());
        List<AppSaveRequest.Parameter> parameters = JSONArray.parseArray(appConfig.getFormVariable(), AppSaveRequest.Parameter.class);
        promptConfig.setParameters(parameters);
        promptConfig.setEmptyResponse(appConfig.getEmptyResponse());
        promptConfig.setPrologue(appConfig.getPrologue());
        promptConfig.setQuote(appConfig.getQuote());
        appEditResponse.setPromptConfig(promptConfig);
        List<AppModelInfo> appModelList = appModelMapper.findListByAppId(app.getAppId());
        AppModelInfo appModelInfo = appModelList.stream().filter(m -> m.getModelType().equals(DefaultModelManager.ModelType.CHAT.getValue())).findFirst().orElse(null);
        if (appModelInfo != null) {
            AppSaveRequest.ModelConfig modelConfig = JSON.parseObject(appModelInfo.getModelConfig(), AppSaveRequest.ModelConfig.class);
            appEditResponse.setModelConfig(modelConfig);
            appEditResponse.setModelId(appModelInfo.getProviderName().concat("@").concat(appModelInfo.getModelName()).concat("@").concat(appModelInfo.getModelType()));
        }
        List<Dataset> datasetList = datasetMapper.findDatasetListByAppId(app.getAppId());
        if (!datasetList.isEmpty()) {
            appEditResponse.setDatasetIds(datasetList.stream().map(Dataset::getDatasetId).toList());
            appEditResponse.setDatasetNames(datasetList.stream().map(Dataset::getName).toList());
        }
        return appEditResponse;
    }

    @Override
    public List<App> list(Long userId) {
        return appMapper.list(userId);
    }

    @Override
    public void saveOrUpdate(App app) {
        if (app.getAppId() == null) {
            appMapper.save(app);
        } else {
            appMapper.update(app);
        }
    }

    @Override
    public List<AppConversation> getAppConversationList(Long appId) {
        return appConversationMapper.findListByAppId(appId, AuthUtils.getUserId());
    }

    @Override
    public Conversation getAppConversation(Long appConversationId) {
        AppConversation appConversation = appConversationMapper.findById(appConversationId);
        List<AppMessage> conversationLastNMessageList = appMessageMapper.getConversationLastNMessageList(appConversationId, 100);

        List<ConversationMessage> conversationMessageList = conversationLastNMessageList.stream().map(appMessage ->
                new ConversationMessage(appMessage.getContent(), appMessage.getRole(), String.valueOf(appMessage.getMessageId()),
                        StringUtils.isBlank(appMessage.getDocumentIds()) ? null : Arrays.stream(appMessage.getDocumentIds().split(",")).toList().stream().map(Long::valueOf).toList().toArray(new Long[0])))
                .toList();

        return new Conversation(appConversation.getAppId(), appConversation.getConversationId(), conversationMessageList);
    }

    @Override
    public void saveAppConversation(AppConversation appConversation) {
        if (appConversation.getConversationId() != null) {
            appConversationMapper.update(appConversation);
        } else {
            appConversationMapper.save(appConversation);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAppConversation(Long appId, Long conversationId) {
        appConversationMapper.delete(appId, conversationId);
        appMessageMapper.deleteByConversationId(appId, conversationId);
    }

    @Deprecated
    Map<String, Object> getFormVariable(String formVariable) {
        JSONArray formVariableArray = JSON.parseArray(formVariable);
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < formVariableArray.size(); i++) {
            JSONObject jsonObject = formVariableArray.getJSONObject(i);
            for (String key : jsonObject.keySet()) {
                map.put(key, jsonObject.get(key));
            }
        }
        return map;
    }

}
