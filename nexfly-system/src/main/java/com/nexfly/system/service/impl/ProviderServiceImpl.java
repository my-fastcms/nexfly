package com.nexfly.system.service.impl;

import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.system.mapper.*;
import com.nexfly.system.model.Provider;
import com.nexfly.system.model.ProviderModel;
import com.nexfly.system.service.ProviderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProviderServiceImpl implements ProviderService, InitializingBean {

    private Map<String, SystemProvider> systemProviderMap = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    private SysProviderMapper sysProviderMapper;

    @Autowired
    private SysProviderModelMapper sysProviderModelMapper;

    @Autowired
    private ProviderModelMapper providerModelMapper;

    @Autowired
    private ProviderMapper providerMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<SystemProviderInfo> getSystemProviderList() {
        return sysProviderMapper.list().stream().map(item -> new SystemProviderInfo(item.getName(), item.getLogo(), Arrays.stream(item.getTags().split(",")).toList(), item.getTags(), item.getStatus())).toList();
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
            SystemProvider systemProvider = getSystemProviderMap().get(provider.getProviderName());
            if (systemProvider != null) {
                List<SystemProviderModel> providerModelList = systemProvider.providerModelList();
                List<Llm> llmList = providerModelList.stream().map(spm -> new Llm(spm.provider(), spm.model(), spm.modelType(), "")).collect(Collectors.toList());
                providerMap.put(provider.getProviderName(), llmList);
            }
        }
        return providerMap;
    }

    @Override
    public Map<String, Object> getUserAddedProviderList() {
        return getStringObjectMap(getSystemProviderMap());
    }

    @NotNull
    private Map<String, Object> getStringObjectMap(Map<String, SystemProvider> systemProviderMap) {
        Map<String, Object> providerMap = new HashMap<>();
        Long orgId = accountMapper.getUserOrg(AuthUtils.getUserId()).getOrgId();
        List<Provider> providerList = getProviderListByOrgId(orgId);
        for (Provider provider : providerList) {
            SystemProvider systemProvider = systemProviderMap.get(provider.getProviderName());
            if (systemProvider != null) {
                List<SystemProviderModel> providerModelList = systemProvider.providerModelList();
                List<Llm> llmList = providerModelList.stream().map(spm -> new Llm(spm.provider(), spm.model(), spm.modelType(), "")).collect(Collectors.toList());
                providerMap.put(provider.getProviderName(), new LlmModel(llmList, systemProvider.tags()));
            }
        }
        return providerMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveApiKey(ApiKeyRequest apiKey) throws Exception {
        SystemProvider systemProvider = getSystemProviderMap().get(apiKey.provider());
        if (systemProvider == null) {
            throw new NexflyException(apiKey.provider() + "not found");
        }

        Long orgId = accountMapper.getUserOrg(AuthUtils.getUserId()).getOrgId();
        Provider provider = providerMapper.getUserProviderByName(orgId, apiKey.provider());
        if (provider == null) {
            provider = new Provider();
            provider.setOrgId(orgId);
            provider.setApiKey(apiKey.apiKey());
            provider.setApiUrl(apiKey.apiUrl());
            provider.setProviderName(apiKey.provider());
            providerMapper.save(provider);

            // save provider model
            List<ProviderModel> providerModelList = systemProvider.providerModelList().stream().map(item -> {
                ProviderModel providerModel = new ProviderModel();
                providerModel.setProviderName(item.provider());
                providerModel.setModelName(item.model());
                providerModel.setModelType(item.modelType());
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

    @Override
    public Map<String, SystemProvider> getSystemProviderMap() {
        return systemProviderMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<SystemProviderModel> sysProviderModelList = sysProviderModelMapper.list().stream().map(item -> new SystemProviderModel(item.getProviderName(), item.getModelName(), item.getModelType(), item.getMaxTokens(), item.getTags())).toList();
        Map<String, List<SystemProviderModel>> map =  sysProviderModelList.stream().collect(Collectors.groupingBy(SystemProviderModel::provider));
        List<SystemProvider> sysProviderList = sysProviderMapper.list().stream().map(item -> new SystemProvider(item.getName(), item.getLogo(), item.getStatus(), item.getTags(), map.get(item.getName()))).toList();
        Map<String, SystemProvider> stringSystemProviderMap = sysProviderList.stream().collect(Collectors.toMap(SystemProvider::provider, SystemProviderResponse -> SystemProviderResponse));
        systemProviderMap.putAll(stringSystemProviderMap);
    }

}
