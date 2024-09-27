package com.nexfly.system.service.impl;

import com.nexfly.ai.common.provider.ProviderManager;
import com.nexfly.ai.common.provider.SystemProvider;
import com.nexfly.ai.common.provider.SystemProviderModel;
import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.system.mapper.AccountMapper;
import com.nexfly.system.mapper.ProviderMapper;
import com.nexfly.system.mapper.ProviderModelMapper;
import com.nexfly.system.model.Provider;
import com.nexfly.system.model.ProviderModel;
import com.nexfly.system.service.ProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderManager providerManager;

    @Autowired
    private ProviderModelMapper providerModelMapper;

    @Autowired
    private ProviderMapper providerMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<SystemProviderInfo> getSystemProviderList() {
        return providerManager.getSystemProviderList().stream().map(item -> new SystemProviderInfo(item.getLabel(), item.getProvider(), item.getConfigurateMethods(), String.join(",", item.getSupportedModelTypes()), "1")).toList();
    }

    @Override
    public List<ProviderModel> getProviderModelListByOrgId(Long orgId) {
        return providerModelMapper.getListByOrgId(orgId);
    }

    @Override
    public List<Provider> getProviderListByOrgId(Long orgId) {
        return providerMapper.getListByOrgId(orgId);
    }

    @Override
    public ProviderModel getProviderModelByOrgAndName(Long orgId, String providerName, String modelName) {
        return providerModelMapper.getProviderModelByOrgAndName(orgId, providerName, modelName);
    }

    @Override
    public Map<String, Object> getUserAvailableProviderList() {
        Map<String, Object> providerMap = new HashMap<>();
        Long orgId = accountMapper.getUserOrg(AuthUtils.getUserId()).getOrgId();
        List<Provider> providerList = getProviderListByOrgId(orgId);
        for (Provider provider : providerList) {
            SystemProvider systemProvider = providerManager.getSystemAvailableProviderMap().get(provider.getProviderName());
            if (systemProvider != null) {
                List<SystemProviderModel> providerModelList = systemProvider.getProviderModelList();
                List<Llm> llmList = providerModelList.stream().map(spm -> new Llm(spm.getProvider(), spm.getModel(), spm.getModelType(), "")).collect(Collectors.toList());
                providerMap.put(provider.getLabel(), llmList);
            }
        }
        return providerMap;
    }

    @Override
    public Map<String, Object> getUserAddedProviderList() {
        return getStringObjectMap(providerManager.getSystemProviderMap());
    }

    @NotNull
    private Map<String, Object> getStringObjectMap(Map<String, SystemProvider> systemProviderMap) {
        Map<String, Object> providerMap = new HashMap<>();
        Long orgId = accountMapper.getUserOrg(AuthUtils.getUserId()).getOrgId();
        List<Provider> providerList = getProviderListByOrgId(orgId);
        for (Provider provider : providerList) {
            SystemProvider systemProvider = systemProviderMap.get(provider.getProviderName());
            if (systemProvider != null) {
                List<SystemProviderModel> providerModelList = systemProvider.getProviderModelList();
                List<Llm> llmList = providerModelList.stream().map(spm -> new Llm(spm.getProvider(), spm.getModel(), spm.getModelType(), "")).collect(Collectors.toList());
                providerMap.put(provider.getLabel(), new LlmModel(llmList, String.join(",", systemProvider.getSupportedModelTypes())));
            }
        }
        return providerMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveApiKey(ApiKey apiKey) throws Exception {
        SystemProvider systemProvider = providerManager.getSystemProviderMap().get(apiKey.provider());
        if (systemProvider == null) {
            throw new Exception(apiKey.provider() + "not found");
        }

        Long orgId = accountMapper.getUserOrg(AuthUtils.getUserId()).getOrgId();
        Provider provider = providerMapper.getUserProviderByName(orgId, apiKey.provider());
        if (provider == null) {
            provider = new Provider();
            provider.setOrgId(orgId);
            provider.setApiKey(apiKey.apiKey());
            provider.setApiUrl(apiKey.apiUrl());
            provider.setProviderName(apiKey.provider());
            provider.setLabel(systemProvider.getLabel());
            providerMapper.save(provider);

            // save provider model
            List<ProviderModel> providerModelList = systemProvider.getProviderModelList().stream().map(item -> {
                ProviderModel providerModel = new ProviderModel();
                providerModel.setProviderName(item.getProvider());
                providerModel.setModelName(item.getModel());
                providerModel.setModelType(item.getModelType());
                providerModel.setOrgId(orgId);
                return providerModel;
            }).toList();

            providerModelMapper.insertBatch(providerModelList);

        } else {
            provider.setApiKey(apiKey.apiKey());
            provider.setApiUrl(apiKey.apiUrl());
            providerMapper.update(provider);
        }

    }

}
