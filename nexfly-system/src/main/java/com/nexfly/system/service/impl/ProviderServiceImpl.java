package com.nexfly.system.service.impl;

import com.nexfly.system.mapper.ProviderMapper;
import com.nexfly.system.mapper.ProviderModelMapper;
import com.nexfly.system.model.Provider;
import com.nexfly.system.model.ProviderModel;
import com.nexfly.system.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderModelMapper providerModelMapper;

    @Autowired
    private ProviderMapper providerMapper;

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

}
