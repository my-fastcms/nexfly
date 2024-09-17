package com.nexfly.system.mapper;

import com.nexfly.system.model.AppMessage;
import org.apache.ibatis.annotations.Param;

public interface AppMessageMapper {
    AppMessage findById(@Param("messageId") Long messageId);

    void save(@Param("appMessage") AppMessage AppMessage);

    void update(@Param("appMessage") AppMessage AppMessage);
}