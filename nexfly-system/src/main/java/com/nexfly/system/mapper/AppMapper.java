package com.nexfly.system.mapper;

import com.nexfly.system.model.App;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppMapper {
    App findById(@Param("appId") Long appId);

    List<App> list(@Param("userId") Long userId);

    void save(@Param("app") App App);

    void update(@Param("app") App App);
}