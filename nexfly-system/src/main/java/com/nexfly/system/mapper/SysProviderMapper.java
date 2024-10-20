package com.nexfly.system.mapper;

import com.nexfly.system.model.SysProvider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysProviderMapper {

    SysProvider findById(@Param("sysProviderId") Long sysProviderId);

    void save(@Param("sysProvider") SysProvider SysProvider);

    void update(@Param("sysProvider") SysProvider SysProvider);

    List<SysProvider> list();

}