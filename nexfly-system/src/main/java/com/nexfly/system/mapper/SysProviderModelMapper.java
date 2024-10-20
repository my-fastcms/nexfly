package com.nexfly.system.mapper;

import com.nexfly.system.model.SysProviderModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysProviderModelMapper {

    SysProviderModel findById(@Param("sysProviderModelId") Long sysProviderModelId);

    void save(@Param("sysProviderModel") SysProviderModel SysProviderModel);

    void update(@Param("sysProviderModel") SysProviderModel SysProviderModel);

    List<SysProviderModel> list();

}