package com.nexfly.system.service;

import com.nexfly.system.model.App;
import com.nexfly.system.model.AppConversation;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
public interface AppService {

    App findById(Long appId);

    List<App> list(Long userId);

    Flux<AppService.ChatResponse> chat(NexflyMessage message) throws Exception;

    List<AppConversation> getAppConversationList(Long appId);

    Conversation getAppConversation(Long appConversationId);

    void saveAppConversation(AppConversation appConversation);

    void deleteAppConversation(Long appId, Long conversationId);

    record ChatResponse(Integer retcode, String retmsge, Object data) {

        public ChatResponse(Object data) {
            this(0, "", data);
        }

    }

    record ChatResponseData(String answer, List<String> reference, String prompt, String id, String audioBinary) {

        public ChatResponseData(String answer, String prompt, String id) {
            this(answer, new ArrayList<>(), prompt, id, "");
        }

    }

    record ConversationMessage(String content, String role, String id) {

    }

    record NexflyMessage(Long conversationId, List<ConversationMessage> messages) {

    }

    record Conversation(Long appId, Long conversationId, List<ConversationMessage> message) {

    }

}
