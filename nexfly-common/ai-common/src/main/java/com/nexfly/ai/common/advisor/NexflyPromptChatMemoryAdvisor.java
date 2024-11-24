package com.nexfly.ai.common.advisor;

import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.MessageAggregator;
import org.springframework.ai.model.Content;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangjun
 * @see org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor
 **/
public class NexflyPromptChatMemoryAdvisor extends AbstractChatMemoryAdvisor<ChatMemory> {

    private static final String DEFAULT_SYSTEM_TEXT_ADVISE = """

			Use the conversation memory from the MEMORY section to provide accurate answers.

			---------------------
			MEMORY:
			{memory}
			---------------------

			""";

    private final String systemTextAdvise;

    public NexflyPromptChatMemoryAdvisor(ChatMemory chatMemory) {
        this(chatMemory, DEFAULT_SYSTEM_TEXT_ADVISE);
    }

    public NexflyPromptChatMemoryAdvisor(ChatMemory chatMemory, String systemTextAdvise) {
        super(chatMemory);
        this.systemTextAdvise = systemTextAdvise;
    }

    public NexflyPromptChatMemoryAdvisor(ChatMemory chatMemory, String defaultConversationId, int chatHistoryWindowSize,
                                   String systemTextAdvise) {
        this(chatMemory, defaultConversationId, chatHistoryWindowSize, systemTextAdvise,
                Advisor.DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER);
    }

    public NexflyPromptChatMemoryAdvisor(ChatMemory chatMemory, String defaultConversationId, int chatHistoryWindowSize,
                                   String systemTextAdvise, int order) {
        super(chatMemory, defaultConversationId, chatHistoryWindowSize, true, order);
        this.systemTextAdvise = systemTextAdvise;
    }

    public static NexflyPromptChatMemoryAdvisor.Builder builder(ChatMemory chatMemory) {
        return new NexflyPromptChatMemoryAdvisor.Builder(chatMemory);
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {

        advisedRequest = this.before(advisedRequest);

        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);

        this.observeAfter(advisedResponse);

        return advisedResponse;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {

        Flux<AdvisedResponse> advisedResponses = this.doNextWithProtectFromBlockingBefore(advisedRequest, chain,
                this::before);

        return new MessageAggregator().aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }

    private AdvisedRequest before(AdvisedRequest request) {

        // 1. Advise system parameters.
        List<Message> memoryMessages = this.getChatMemoryStore()
                .get(this.doGetConversationId(request.adviseContext()),
                        this.doGetChatMemoryRetrieveSize(request.adviseContext()));

        String memory = (memoryMessages != null) ? memoryMessages.stream()
                .filter(m -> m.getMessageType() == MessageType.USER || m.getMessageType() == MessageType.ASSISTANT)
                .map(m -> m.getMessageType() + ":" + ((Content) m).getContent())
                .collect(Collectors.joining(System.lineSeparator())) : "";

        Map<String, Object> advisedSystemParams = new HashMap<>(request.systemParams());
        advisedSystemParams.put("memory", memory);

        // 2. Advise the system text.
        String advisedSystemText = request.systemText() + System.lineSeparator() + this.systemTextAdvise;

        // 3. Create a new request with the advised system text and parameters.
        AdvisedRequest advisedRequest = AdvisedRequest.from(request)
                .withSystemText(advisedSystemText)
                .withSystemParams(advisedSystemParams)
                .build();

        // 4. Add the new user input to the conversation memory.

        /**
         * 此处把最后一条消息的metadata设置进去，spring-ai 直接 new UserMessage(request.userText(), request.media())
         */
        Message message = request.messages().get(request.messages().size() - 1);
        UserMessage userMessage = new UserMessage(request.userText(), request.media(), message.getMetadata());

        this.getChatMemoryStore().add(this.doGetConversationId(request.adviseContext()), userMessage);

        return advisedRequest;
    }

    private void observeAfter(AdvisedResponse advisedResponse) {

        List<Message> assistantMessages = advisedResponse.response()
                .getResults()
                .stream()
                .map(g -> (Message) g.getOutput())
                .toList();

        this.getChatMemoryStore().add(this.doGetConversationId(advisedResponse.adviseContext()), assistantMessages);
    }

    public static class Builder extends AbstractChatMemoryAdvisor.AbstractBuilder<ChatMemory> {

        private String systemTextAdvise = DEFAULT_SYSTEM_TEXT_ADVISE;

        protected Builder(ChatMemory chatMemory) {
            super(chatMemory);
        }

        public NexflyPromptChatMemoryAdvisor.Builder withSystemTextAdvise(String systemTextAdvise) {
            this.systemTextAdvise = systemTextAdvise;
            return this;
        }

        public NexflyPromptChatMemoryAdvisor build() {
            return new NexflyPromptChatMemoryAdvisor(this.chatMemory, this.conversationId, this.chatMemoryRetrieveSize,
                    this.systemTextAdvise, this.order);
        }

    }

}
