package com.nexfly.system.mapper;

import com.nexfly.api.system.bean.AppModelInfo;
import com.nexfly.system.model.AppModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppModelMapper {

    List<AppModelInfo> findListByAppId(@Param("appId") Long appId);

    AppModel findById(@Param("appModelId") Long appModelId);

    void save(@Param("appModel") AppModel AppModel);

    void update(@Param("appModel") AppModel AppModel);
}