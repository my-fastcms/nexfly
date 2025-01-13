package com.nexfly.ai.common.advisor;

import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionTextParser;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author wangjun
 * @Date 2024/10/21
 **/
public class NexflyQuestionAnswerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    public static final String RETRIEVED_DOCUMENTS = "qa_retrieved_documents";

    public static final String FILTER_EXPRESSION = "qa_filter_expression";

    private static final String DEFAULT_USER_TEXT_ADVISE = """

			Context information is below, surrounded by ---------------------

			---------------------
			{question_answer_context}
			---------------------

			Given the context and provided history information and not prior knowledge,
			reply to the user comment. If the answer is not in the context, inform
			the user that you can't answer the question.
			""";

    private static final int DEFAULT_ORDER = 0;

    private final VectorStore vectorStore;

    private final String userTextAdvise;

    private final SearchRequest searchRequest;

    private final boolean protectFromBlocking;

    private final int order;

    /**
     * The QuestionAnswerAdvisor retrieves context information from a Vector Store and
     * combines it with the user's text.
     * @param vectorStore The vector store to use
     */
    public NexflyQuestionAnswerAdvisor(VectorStore vectorStore) {
        this(vectorStore, SearchRequest.builder().build(), DEFAULT_USER_TEXT_ADVISE);
    }

    /**
     * The QuestionAnswerAdvisor retrieves context information from a Vector Store and
     * combines it with the user's text.
     * @param vectorStore The vector store to use
     * @param searchRequest The search request defined using the portable filter
     * expression syntax
     */
    public NexflyQuestionAnswerAdvisor(VectorStore vectorStore, SearchRequest searchRequest) {
        this(vectorStore, searchRequest, DEFAULT_USER_TEXT_ADVISE);
    }

    /**
     * The QuestionAnswerAdvisor retrieves context information from a Vector Store and
     * combines it with the user's text.
     * @param vectorStore The vector store to use
     * @param searchRequest The search request defined using the portable filter
     * expression syntax
     * @param userTextAdvise the user text to append to the existing user prompt. The text
     * should contain a placeholder named "question_answer_context".
     */
    public NexflyQuestionAnswerAdvisor(VectorStore vectorStore, SearchRequest searchRequest, String userTextAdvise) {
        this(vectorStore, searchRequest, userTextAdvise, true);
    }

    /**
     * The QuestionAnswerAdvisor retrieves context information from a Vector Store and
     * combines it with the user's text.
     * @param vectorStore The vector store to use
     * @param searchRequest The search request defined using the portable filter
     * expression syntax
     * @param userTextAdvise the user text to append to the existing user prompt. The text
     * should contain a placeholder named "question_answer_context".
     * @param protectFromBlocking if true the advisor will protect the execution from
     * blocking threads. If false the advisor will not protect the execution from blocking
     * threads. This is useful when the advisor is used in a non-blocking environment. It
     * is true by default.
     */
    public NexflyQuestionAnswerAdvisor(VectorStore vectorStore, SearchRequest searchRequest, String userTextAdvise,
                                 boolean protectFromBlocking) {
        this(vectorStore, searchRequest, userTextAdvise, protectFromBlocking, DEFAULT_ORDER);
    }

    /**
     * The QuestionAnswerAdvisor retrieves context information from a Vector Store and
     * combines it with the user's text.
     * @param vectorStore The vector store to use
     * @param searchRequest The search request defined using the portable filter
     * expression syntax
     * @param userTextAdvise the user text to append to the existing user prompt. The text
     * should contain a placeholder named "question_answer_context".
     * @param protectFromBlocking if true the advisor will protect the execution from
     * blocking threads. If false the advisor will not protect the execution from blocking
     * threads. This is useful when the advisor is used in a non-blocking environment. It
     * is true by default.
     * @param order the order of the advisor.
     */
    public NexflyQuestionAnswerAdvisor(VectorStore vectorStore, SearchRequest searchRequest, String userTextAdvise,
                                 boolean protectFromBlocking, int order) {

        Assert.notNull(vectorStore, "The vectorStore must not be null!");
        Assert.notNull(searchRequest, "The searchRequest must not be null!");
        Assert.hasText(userTextAdvise, "The userTextAdvise must not be empty!");

        this.vectorStore = vectorStore;
        this.searchRequest = searchRequest;
        this.userTextAdvise = userTextAdvise;
        this.protectFromBlocking = protectFromBlocking;
        this.order = order;
    }

    public static NexflyQuestionAnswerAdvisor.Builder builder(VectorStore vectorStore) {
        return new NexflyQuestionAnswerAdvisor.Builder(vectorStore);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {

        AdvisedRequest advisedRequest2 = before(advisedRequest);

        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest2);

        return after(advisedResponse);
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {

        // This can be executed by both blocking and non-blocking Threads
        // E.g. a command line or Tomcat blocking Thread implementation
        // or by a WebFlux dispatch in a non-blocking manner.
        Flux<AdvisedResponse> advisedResponses = (this.protectFromBlocking) ?
                // @formatter:off
                Mono.just(advisedRequest)
                        .publishOn(Schedulers.boundedElastic())
                        .map(this::before)
                        .flatMapMany(request -> chain.nextAroundStream(request))
                : chain.nextAroundStream(before(advisedRequest));
        // @formatter:on

        return advisedResponses.map(ar -> {
            if (onFinishReason().test(ar)) {
                ar = after(ar);
            }
            return ar;
        });
    }

    private AdvisedRequest before(AdvisedRequest request) {
        var context = new HashMap<>(request.adviseContext());

        // 1. 搜索相似文档
        var searchRequestToUse = SearchRequest.from(this.searchRequest)
                .query(request.userText())
                .filterExpression(doGetFilterExpression(context))
                .build();

        List<Document> documents = this.vectorStore.similaritySearch(searchRequestToUse);

        String documentContext;

        // 2. 如果没有找到文档，返回 null 表示无法继续处理
        if (documents == null || documents.isEmpty()) {
            documentContext = "抱歉，未能找到相关信息，因此无法回答您的问题。"; // 搜索不到相关内容时，不传递给大模型
        } else {
            // 3. 构建上下文
            documentContext = documents.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining(System.lineSeparator()));
        }

        context.put(RETRIEVED_DOCUMENTS, documents);

        // 4. 将上下文插入提示
        String advisedUserText = request.userText() + System.lineSeparator() + this.userTextAdvise;
        Map<String, Object> advisedUserParams = new HashMap<>(request.userParams());
        advisedUserParams.put("question_answer_context", documentContext);

        // 5. 更新请求
        AdvisedRequest advisedRequest = AdvisedRequest.from(request)
                .userText(advisedUserText)
                .userParams(advisedUserParams)
                .adviseContext(context)
                .build();

        return advisedRequest;
    }

    private AdvisedResponse after(AdvisedResponse advisedResponse) {
        // 检查是否需要使用默认回复
        List<Document> retrievedDocuments = (List<Document>) advisedResponse.adviseContext().get(RETRIEVED_DOCUMENTS);
        if (retrievedDocuments == null || retrievedDocuments.isEmpty()) {
            // 检索到的文档为空，替换大模型的回复
            ChatResponse.Builder chatResponseBuilder = ChatResponse.builder()
                    .from(advisedResponse.response())
                    .withGenerations(List.of(new Generation(new AssistantMessage("抱歉，未能找到相关信息，因此无法回答您的问题。"))));
            return new AdvisedResponse(chatResponseBuilder.build(), advisedResponse.adviseContext());
        }

        // 否则正常返回响应，添加元数据
        ChatResponse.Builder chatResponseBuilder = ChatResponse.builder()
                .from(advisedResponse.response())
                .withMetadata(RETRIEVED_DOCUMENTS, retrievedDocuments);
        return new AdvisedResponse(chatResponseBuilder.build(), advisedResponse.adviseContext());
    }

    protected Filter.Expression doGetFilterExpression(Map<String, Object> context) {
        if (!context.containsKey(FILTER_EXPRESSION)
                || !StringUtils.hasText(context.get(FILTER_EXPRESSION).toString())) {
            return this.searchRequest.getFilterExpression();
        }
        return new FilterExpressionTextParser().parse(context.get(FILTER_EXPRESSION).toString());
    }

    private Predicate<AdvisedResponse> onFinishReason() {
        return (advisedResponse) -> advisedResponse.response()
                .getResults()
                .stream()
                .filter(result -> result != null && result.getMetadata() != null
                        && StringUtils.hasText(result.getMetadata().getFinishReason()))
                .findFirst()
                .isPresent();
    }

    public static final class Builder {

        private final VectorStore vectorStore;

        private SearchRequest searchRequest = SearchRequest.builder().build();

        private String userTextAdvise = DEFAULT_USER_TEXT_ADVISE;

        private boolean protectFromBlocking = true;

        private int order = DEFAULT_ORDER;

        private Builder(VectorStore vectorStore) {
            Assert.notNull(vectorStore, "The vectorStore must not be null!");
            this.vectorStore = vectorStore;
        }

        public Builder withSearchRequest(SearchRequest searchRequest) {
            Assert.notNull(searchRequest, "The searchRequest must not be null!");
            this.searchRequest = searchRequest;
            return this;
        }

        public Builder withUserTextAdvise(String userTextAdvise) {
            Assert.hasText(userTextAdvise, "The userTextAdvise must not be empty!");
            this.userTextAdvise = userTextAdvise;
            return this;
        }

        public Builder withProtectFromBlocking(boolean protectFromBlocking) {
            this.protectFromBlocking = protectFromBlocking;
            return this;
        }

        public Builder withOrder(int order) {
            this.order = order;
            return this;
        }

        public NexflyQuestionAnswerAdvisor build() {
            return new NexflyQuestionAnswerAdvisor(this.vectorStore, this.searchRequest, this.userTextAdvise,
                    this.protectFromBlocking, this.order);
        }

    }

}

