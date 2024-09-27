package com.nexfly.system.mapper;

import com.nexfly.system.model.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderMapper {
    Provider findById(@Param("providerId") Long providerId);

    void save(@Param("provider") Provider Provider);

    void update(@Param("provider") Provider Provider);

    List<Provider> getListByOrgId(@Param("orgId") Long orgId);

    Provider getUserProviderByName(@Param("orgId") Long orgId, @Param("providerName") String providerName);

}