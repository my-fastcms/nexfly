package com.nexfly.system.mapper;

import com.nexfly.system.model.AppConversation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppConversationMapper {

    List<AppConversation> findListByAppId(@Param("appId") Long appId);

    AppConversation findById(@Param("conversationId") Long conversationId);

    void save(@Param("appConversation") AppConversation AppConversation);

    void update(@Param("appConversation") AppConversation AppConversation);

    void delete(@Param("appId") Long appId, @Param("conversationId") Long conversationId);

}