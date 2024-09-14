package com.nexfly.system.mapper;

import com.nexfly.system.model.App;
import org.apache.ibatis.annotations.Param;

public interface AppMapper {
    App findById(@Param("appId") Long appId);

    void save(@Param("app") App App);

    void update(@Param("app") App App);
}