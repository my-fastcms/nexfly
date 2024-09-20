package com.nexfly.system.service.impl;

import com.nexfly.ai.common.provider.ProviderManager;
import com.nexfly.ai.common.provider.SystemProvider;
import com.nexfly.ai.common.provider.SystemProviderModel;
import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.system.mapper.AccountMapper;
import com.nexfly.system.mapper.ProviderMapper;
import com.nexfly.system.mapper.ProviderModelMapper;
import com.nexfly.system.model.Provider;
import com.nexfly.system.model.ProviderModel;
import com.nexfly.system.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ProviderModel findModelById(Long modelId) {
        return providerModelMapper.findById(modelId);
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
    public List<ProviderModel> getProviderModelList() {
        return providerModelMapper.list();
    }

    @Override
    public List<Provider> getProviderList() {
        return providerMapper.list();
    }

    @Override
    public ProviderModel getProviderModelByOrgAndName(Long orgId, String modelName) {
        return providerModelMapper.getProviderModelByOrgAndName(orgId, modelName);
    }

    @Override
    public void saveProviderModel(ProviderModel providerModel) throws NexflyException {
        ProviderModel model = providerModelMapper.getProviderModelByOrgAndName(providerModel.getOrgId(), providerModel.getModelName());
        if (model == null) {
            providerModelMapper.save(providerModel);
        } else {
            providerModelMapper.update(providerModel);
        }
    }

    @Override
    public Map<String, Object> getUserAvailableProviderList() {

        Map<String, SystemProvider> systemProviderMap = providerManager.getSystemProviderMap();

        Map<String, Object> providerMap = new HashMap<>();
        Long orgId = accountMapper.getUserOrg(AuthUtils.getUserId()).getOrgId();
        List<Provider> providerList = getProviderListByOrgId(orgId);
        for (Provider provider : providerList) {
            SystemProvider systemProvider = systemProviderMap.get(provider.getProviderName());
            if (systemProvider != null) {
                List<SystemProviderModel> providerModelList = systemProvider.getProviderModelList();
                List<Llm> llmList = providerModelList.stream().map(spm -> new Llm(spm.getModel(), spm.getModelType(), "")).collect(Collectors.toList());
                providerMap.put(provider.getLabel(), new LlmModel(llmList, String.join(",", systemProvider.getSupportedModelTypes())));
            }
        }
        return providerMap;
    }

}
