package com.nexfly.system.manager;

import com.nexfly.ai.common.model.CreateModel;
import com.nexfly.ai.common.model.ModelFactory;
import com.nexfly.system.model.Provider;
import com.nexfly.system.model.ProviderModel;
import com.nexfly.system.service.ProviderService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultModelManager implements ModelManager, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private ProviderService providerService;

    @Override
    public ChatClient getChatClient(Long orgId, String modelName) {
        ChatModel chatModel = getChatModel(orgId, modelName);
        return ChatClient.builder(chatModel).build();
    }

    @Override
    public ChatModel getChatModel(Long orgId, String modelName) {
        return (ChatModel) getModel(orgId, modelName);
    }

    @Override
    public EmbeddingModel getEmbeddingModel(Long orgId, String modelName) {
        return (EmbeddingModel) getModel(orgId, modelName);
    }

    @Override
    public ImageModel getImageModel(Long orgId, String modelName) {
        return (ImageModel) getModel(orgId, modelName);
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
    public void afterPropertiesSet() throws Exception {
        initSystemProviderModel();
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    synchronized Object getModel(Long orgId, String modelName) {
        final CreateModel createModel = getCreateModel(orgId, modelName);
        try {
          return createModel(createModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载系统支持的大模型
     * @throws Exception
     */
    void initSystemProviderModel() throws Exception {

    }

    CreateModel getCreateModel(Long orgId, String modelName) {
        ProviderModel providerModel = providerService.getProviderModelByOrgAndName(orgId, modelName);
        List<Provider> providerList = providerService.getProviderListByOrgId(orgId);
        Map<String, Provider> providerMap = providerList.stream().collect(Collectors.toMap(Provider::getProviderName, item -> item));
        Provider provider = providerMap.get(providerModel.getProviderName());
        return new CreateModel(providerModel.getOrgId(), providerModel.getProviderName(), providerModel.getModelName(), providerModel.getModelType(), providerModel.getModelConfig(), provider.getConfig());
    }

    @Deprecated
    String getCashKey(CreateModel createModel) {
        return createModel.getOrgId() + "-" + createModel.getProviderName().concat("-").concat(createModel.getModelType());
    }

}
