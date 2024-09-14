package com.nexfly.system.mapper;

import com.nexfly.system.model.Organization;
import org.apache.ibatis.annotations.Param;

public interface OrganizationMapper {
    Organization findById(@Param("orgId") Long orgId);

    void save(@Param("organization") Organization Organization);

    void update(@Param("organization") Organization Organization);
}