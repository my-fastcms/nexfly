package com.nexfly.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nexfly.ai.common.function.FunctionManager;
import com.nexfly.ai.common.vectorstore.VectorStoreFactory;
import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.system.manager.ModelManager;
import com.nexfly.system.mapper.*;
import com.nexfly.system.model.*;
import com.nexfly.system.service.AppService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static com.nexfly.common.core.constants.NexflyConstants.USER_ID;

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
    private ProviderModelMapper providerModelMapper;

    @Autowired
    private AppConfigMapper appConfigMapper;

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private VectorStoreFactory vectorStoreFactory;

    @Autowired
    private FunctionManager functionManager;

    @Override
    public Flux<String> chat(Long appId, String message) throws Exception {
        App app = appMapper.findById(appId);

        // 查询app关联的大语言模型
        AppModel appModel = appModelMapper.findByAppId(app.getAppId());
        ProviderModel appProviderModel = providerModelMapper.findById(appModel.getModelId());
        String modelName = appProviderModel.getModelName();
        ChatModel chatModel = modelManager.getChatModel(app.getOrgId(), modelName);

        // 查找 app配置信息
        AppConfig appConfig = appConfigMapper.findByAppId(app.getAppId());
        // 系统消息提示词
        String system = appConfig.getPrePrompt();

        // 查找app关联的数据集配置信息
        List<Dataset> datasetList = datasetMapper.findDatasetListByAppId(app.getAppId());
        // 根据dataset构建List<RequestResponseAdvisor>
        List<Advisor> requestResponseAdvisorList = new ArrayList<>();
        requestResponseAdvisorList.add(new PromptChatMemoryAdvisor(new InMemoryChatMemory()));
        for (Dataset dataset : datasetList) {
            ProviderModel providerModel = providerModelMapper.findById(dataset.getEmbedModelId());
            EmbeddingModel embeddingModel = modelManager.getEmbeddingModel(app.getOrgId(), providerModel.getModelName());
            var qaAdvisor = new QuestionAnswerAdvisor(vectorStoreFactory.getVectorStore(dataset.getVsIndexNodeId(), embeddingModel),
                    SearchRequest.defaults().withSimilarityThreshold(0.65).withTopK(6), system);
            requestResponseAdvisorList.add(qaAdvisor);
        }

        // 变量
        JSONObject formVariable = JSON.parseObject(appConfig.getFormVariable());
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(system);
        Message systemMessage = formVariable == null ? systemPromptTemplate.createMessage() : systemPromptTemplate.createMessage(formVariable);

        ChatClient.ChatClientRequestSpec chatClientRequestSpec = ChatClient.builder(chatModel)
                .build()
                .prompt()
                .system(systemMessage.getContent())
                .user(message);

        functionManager.getFunctionList().forEach(item -> chatClientRequestSpec.function(item.name(), item.description(), item.function()));

        return chatClientRequestSpec
                .advisors(a -> a.param(USER_ID, AuthUtils.getUserId()))
                .advisors(requestResponseAdvisorList)
                .stream()
                .content();
    }

}
