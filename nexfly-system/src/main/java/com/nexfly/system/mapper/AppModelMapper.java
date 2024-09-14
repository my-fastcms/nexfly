package com.nexfly.system.mapper;

import com.nexfly.system.model.AppModel;
import org.apache.ibatis.annotations.Param;

public interface AppModelMapper {

    AppModel findByAppId(@Param("appId") Long appId);

    AppModel findById(@Param("appModelId") Long appModelId);

    void save(@Param("appModel") AppModel AppModel);

    void update(@Param("appModel") AppModel AppModel);
}