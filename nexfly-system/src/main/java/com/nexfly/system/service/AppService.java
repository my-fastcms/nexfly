package com.nexfly.system.service;

import com.nexfly.api.system.bean.AppEditResponse;
import com.nexfly.api.system.bean.AppSaveRequest;
import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.system.model.App;
import com.nexfly.system.model.AppConversation;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
public interface AppService {

    Boolean save(AppSaveRequest appParam) throws NexflyException;

    AppEditResponse findById(Long appId);

    List<App> list(Long userId);

    void saveOrUpdate(App app);

    Flux<AppService.ChatResponse> chat(NexflyMessage message) throws Exception;

    List<AppConversation> getAppConversationList(Long appId);

    Conversation getAppConversation(Long appConversationId);

    void saveAppConversation(AppConversation appConversation);

    void deleteAppConversation(Long appId, Long conversationId);

    record ChatResponse(Integer code, String message, Object data) {

        public ChatResponse(Object data) {
            this(0, "", data);
        }

    }

    record ChatResponseData(String answer, List<String> reference, String id, ChatResponseMetadata metadata, String audioBinary) {

        public ChatResponseData(String answer, ChatResponseMetadata metadata, String id) {
            this(answer, new ArrayList<>(), id, metadata, "");
        }

    }

    record ConversationMessage(String content, String role, String id) {

    }

    record NexflyMessage(Long conversationId, List<ConversationMessage> messages) {

    }

    record Conversation(Long appId, Long conversationId, List<ConversationMessage> message) {

    }

}
