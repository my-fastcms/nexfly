package com.nexfly.system.service;

import com.nexfly.system.model.Provider;
import com.nexfly.system.model.ProviderModel;

import java.util.List;

/**
 * @author wangjun
 */
public interface ProviderService {

    ProviderModel findModelById(Long modelId);

    /**
     * 获取当前用户组织下已添加的大模型列表
     * @param orgId
     * @return
     */
    List<ProviderModel> getProviderModelListByOrgId(Long orgId);

    List<Provider> getProviderListByOrgId(Long orgId);

    List<ProviderModel> getProviderModelList();

    List<Provider> getProviderList();

    ProviderModel getProviderModelByOrgAndName(Long orgId, String modelType);

}
