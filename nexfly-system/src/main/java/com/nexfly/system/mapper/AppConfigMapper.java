package com.nexfly.system.mapper;

import com.nexfly.system.model.AppConfig;
import org.apache.ibatis.annotations.Param;

public interface AppConfigMapper {

    AppConfig findByAppId(@Param("appId") Long appId);

    AppConfig findById(@Param("configId") Long configId);

    void save(@Param("appConfig") AppConfig AppConfig);

    void update(@Param("appConfig") AppConfig AppConfig);
}