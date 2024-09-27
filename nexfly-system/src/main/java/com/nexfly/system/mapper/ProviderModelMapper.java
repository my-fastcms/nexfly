package com.nexfly.system.mapper;

import com.nexfly.system.model.ProviderModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderModelMapper {
    ProviderModel findById(@Param("modelId") Long modelId);

    void save(@Param("providerModel") ProviderModel ProviderModel);

    void update(@Param("providerModel") ProviderModel ProviderModel);

    List<ProviderModel> getListByOrgId(@Param("orgId") Long orgId);

    ProviderModel getProviderModelByOrgAndName(@Param("orgId") Long orgId, @Param("providerName") String providerName, @Param("modelName") String modelName);

    void insertBatch(@Param("providerModelList") List<ProviderModel> providerModelList);

}