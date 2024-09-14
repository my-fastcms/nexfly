package com.nexfly.system.mapper;

import com.nexfly.system.model.ProviderModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderModelMapper {
    ProviderModel findById(@Param("modelId") Long modelId);

    void save(@Param("providerModel") ProviderModel ProviderModel);

    void update(@Param("providerModel") ProviderModel ProviderModel);

    List<ProviderModel> getListByOrgId(@Param("orgId") Long orgId);

    List<ProviderModel> list();

    ProviderModel getProviderModelByOrgAndName(@Param("orgId") Long orgId, @Param("modelName") String modelName);

}