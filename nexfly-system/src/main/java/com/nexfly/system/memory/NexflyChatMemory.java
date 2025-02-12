package com.nexfly.system.memory;

import com.nexfly.system.mapper.AppMessageMapper;
import com.nexfly.system.model.AppMessage;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.nexfly.common.core.constants.NexflyConstants.DOCUMENT_IDS;

/**
 * @Author wangjun
 * @Date 2024/9/18
 **/
public class NexflyChatMemory implements ChatMemory {

    private final Long userId;

    private final Long appId;

    private final AppMessageMapper appMessageMapper;

    public NexflyChatMemory(Long userId, Long appId, AppMessageMapper appMessageMapper) {
        this.userId = userId;
        this.appId = appId;
        this.appMessageMapper = appMessageMapper;
    }

    @Override
    public void add(String conversationId, Message message) {
        AppMessage appMessage = new AppMessage();
        appMessage.setAppId(appId);
        appMessage.setConversationId(Long.valueOf(conversationId));
        appMessage.setRole(message.getMessageType().getValue());
        appMessage.setContent(message.getContent());
        appMessage.setCreateBy(userId);
        appMessage.setUpdateBy(userId);
        Object o = message.getMetadata().get(DOCUMENT_IDS);
        if (o != null) {
            String documentIds = Arrays.stream(((Long[]) o)).map(String::valueOf).collect(Collectors.joining(","));
            appMessage.setDocumentIds(documentIds);
        }
        appMessageMapper.save(appMessage);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        messages.forEach(message -> add(conversationId, message));
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        // 取最新lastN条消息返回
        List<AppMessage> conversationLastNMessageList = appMessageMapper.getConversationLastNMessageList(Long.valueOf(conversationId), lastN);

        List<Message> messageList = new ArrayList<>();

        conversationLastNMessageList.forEach(msg -> {
            Message message = null;
            if (MessageType.USER.getValue().equals(msg.getRole())) {
                message = new UserMessage(msg.getContent());
            } else if (MessageType.ASSISTANT.getValue().equals(msg.getRole())) {
                message = new AssistantMessage(msg.getContent());
            }
            if (message != null) {
                messageList.add(message);
            }
        });

        return messageList;
    }

    @Override
    public void clear(String conversationId) {

    }

}
