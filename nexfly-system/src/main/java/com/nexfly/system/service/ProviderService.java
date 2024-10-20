package com.nexfly.system.service;

import com.nexfly.system.model.Provider;
import com.nexfly.system.model.ProviderModel;

import java.util.List;
import java.util.Map;

/**
 * @author wangjun
 */
public interface ProviderService {

    Map<String, SystemProvider> getSystemProviderMap();

    List<SystemProviderInfo> getSystemProviderList();

    /**
     * 获取当前用户组织下已添加的大模型列表
     * @param orgId
     * @return
     */
    List<ProviderModel> getProviderModelListByOrgId(Long orgId);

    List<Provider> getProviderListByOrgId(Long orgId);

    ProviderModel getProviderModelByOrgAndName(Long orgId, String providerName, String modelName);

    /**
     * 获取用户可用的大模型列表
     * @return
     */
    Map<String, Object> getUserAvailableProviderList();

    /**
     * 获取用户已添加配置的大模型列表
     * @return
     */
    Map<String, Object> getUserAddedProviderList();

    void saveApiKey(ApiKeyRequest apiKey) throws Exception;

    record Llm(String provider, String name, String type, String usedToken) {

    }

    record LlmModel(List<Llm> llm, String tags) {

    }

    record ApiKeyRequest(String provider, String apiKey, String apiUrl) {

    }

    record SystemProviderInfo(String provider, String logo, List<String> modelTypes, String tags, Integer status) {

    }

    record SystemProvider(String provider, String logo, Integer status, String tags, List<SystemProviderModel> providerModelList) {

    }

    record SystemProviderModel(String provider, String model, String modelType, Integer maxTokens, String tags) {

    }

}
