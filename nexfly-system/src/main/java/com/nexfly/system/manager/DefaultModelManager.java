package com.nexfly.system.manager;

import com.nexfly.ai.common.model.CreateModel;
import com.nexfly.ai.common.model.ModelFactory;
import com.nexfly.ai.common.provider.ProviderManager;
import com.nexfly.ai.common.provider.SystemProvider;
import com.nexfly.ai.common.provider.SystemProviderModel;
import com.nexfly.api.system.bean.AppModelInfo;
import com.nexfly.system.mapper.AppMapper;
import com.nexfly.system.mapper.AppModelMapper;
import com.nexfly.system.mapper.DatasetMapper;
import com.nexfly.system.mapper.ProviderModelMapper;
import com.nexfly.system.model.App;
import com.nexfly.system.model.Dataset;
import com.nexfly.system.model.Provider;
import com.nexfly.system.model.ProviderModel;
import com.nexfly.system.service.ProviderService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultModelManager implements ModelManager, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private AppModelMapper appModelMapper;

    @Autowired
    private DatasetMapper datasetMapper;

    @Autowired
    private ProviderModelMapper providerModelMapper;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private ProviderManager providerManager;

    @Override
    public ChatModel getChatModel(Long appId) {
        return (ChatModel) getModel(appId, ModelType.CHAT.getValue());
    }

    @Override
    public EmbeddingModel getEmbeddingModel(Long datasetId) {
        return (EmbeddingModel) getEmbeddModel(datasetId);
    }

    @Override
    public ImageModel getImageModel(Long appId) {
        return (ImageModel) getModel(appId, ModelType.TTS.getValue());
    }

    @Override
    public synchronized Object createModel(CreateModel createModel) throws Exception {
        if (StringUtils.isBlank(createModel.getModelName())) {
            return null;
        }
        ModelFactory modelCreateService;
        try {
            modelCreateService = (ModelFactory) applicationContext.getBean(createModel.getProviderName() + "-" + createModel.getModelType());
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
        return modelCreateService.getInstance(createModel);
    }

    @Override
    public Boolean checkApiKey(ProviderService.ApiKey apiKey) {
        SystemProvider systemProvider = providerManager.getSystemProviderMap().get(apiKey.provider());
        List<SystemProviderModel> providerModelList = systemProvider.getProviderModelList();
        String model = providerModelList.stream().filter(pm -> pm.getModelType().equals(DefaultModelManager.ModelType.CHAT.getValue())).toList().get(0).getModel();

        CreateModel createModel = new CreateModel(apiKey.apiKey(), apiKey.apiUrl(), apiKey.provider(), model, DefaultModelManager.ModelType.CHAT.getValue());
        try {
            ChatModel chatModel = (ChatModel) createModel(createModel);
            chatModel.call("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    synchronized Object getModel(Long appId, String modelType) {
        final CreateModel createModel = getCreateModel(appId, modelType);
        try {
            return createModel(createModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    synchronized Object getEmbeddModel(Long datasetId) {
        Dataset dataset = datasetMapper.findById(datasetId);
        ProviderModel providerModel = providerModelMapper.getProviderModelByOrgAndName(dataset.getOrgId(), dataset.getProvider(), dataset.getModel());
        Provider provider = getProvider(providerModel.getOrgId(), providerModel.getProviderName());
        final CreateModel createModel = new CreateModel(provider.getApiKey(), provider.getApiUrl(), providerModel.getProviderName(), providerModel.getModelName(), providerModel.getModelType());
        try {
          return createModel(createModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    CreateModel getCreateModel(Long appId, String modelType) {
        List<AppModelInfo> appModelList = appModelMapper.findListByAppId(appId);
        List<AppModelInfo> list = appModelList.stream().filter(item -> item.getModelType().equals(modelType)).toList();
        if (list.isEmpty()) {
            throw new RuntimeException("modelType:" + modelType + "is null");
        }
        AppModelInfo appModelInfo = list.get(0);
        App app = appMapper.findById(appId);
        Provider provider = getProvider(app.getOrgId(), appModelInfo.getProviderName());
        return new CreateModel(provider.getApiKey(), provider.getApiUrl(), appModelInfo.getProviderName(), appModelInfo.getModelName(), appModelInfo.getModelType(), appModelInfo.getModelConfig());
    }

    Provider getProvider(Long orgId, String providerName) {
        List<Provider> providerList = providerService.getProviderListByOrgId(orgId);
        Map<String, Provider> providerMap = providerList.stream().collect(Collectors.toMap(Provider::getProviderName, item -> item));
        return providerMap.get(providerName);
    }

    public enum ModelType {

        CHAT("chat"),

        EMBEDDING("embedding"),

        TTS("tts"),

        SPEECH2TEXT("speech2text"),

        IMAGE2TEXT("image2text"),

        RERANK("rerank");

        public final String value;

        ModelType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

    }

}
