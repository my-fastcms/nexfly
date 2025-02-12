package com.nexfly.ai.tongyi;

import com.nexfly.ai.tongyi.api.TongYiAiApi;
import com.nexfly.ai.tongyi.api.TongYiAiApiConstants;
import com.nexfly.ai.tongyi.metadata.TongYiAiUsage;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.EmptyUsage;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.model.*;
import org.springframework.ai.chat.observation.ChatModelObservationContext;
import org.springframework.ai.chat.observation.ChatModelObservationConvention;
import org.springframework.ai.chat.observation.ChatModelObservationDocumentation;
import org.springframework.ai.chat.observation.DefaultChatModelObservationConvention;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackResolver;
import org.springframework.ai.openai.metadata.support.OpenAiResponseHeaderExtractor;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TongYiChatModel extends AbstractToolCallSupport implements ChatModel {

    private static final Logger logger = LoggerFactory.getLogger(TongYiChatModel.class);

    private static final ChatModelObservationConvention DEFAULT_OBSERVATION_CONVENTION = new DefaultChatModelObservationConvention();

    /**
     * The default options used for the chat completion requests.
     */
    private final TongYiChatOptions defaultOptions;

    /**
     * The retry template used to retry the OpenAI API calls.
     */
    private final RetryTemplate retryTemplate;

    private final TongYiAiApi tongYiAiApi;

    /**
     * Observation registry used for instrumentation.
     */
    private final ObservationRegistry observationRegistry;

    private ChatModelObservationConvention observationConvention = DEFAULT_OBSERVATION_CONVENTION;

    public TongYiChatModel(TongYiAiApi tongYiAiApi) {
        this(tongYiAiApi, TongYiChatOptions.builder().withModel(TongYiAiApi.DEFAULT_CHAT_MODEL).withTemperature(0.7).build());
    }

    public TongYiChatModel(TongYiAiApi tongYiAiApi, TongYiChatOptions options) {
        this(tongYiAiApi, options, null, RetryUtils.DEFAULT_RETRY_TEMPLATE);
    }

    public TongYiChatModel(TongYiAiApi tongYiAiApi, TongYiChatOptions options,
                           FunctionCallbackResolver functionCallbackResolver, RetryTemplate retryTemplate) {
        this(tongYiAiApi, options, functionCallbackResolver, List.of(), retryTemplate);
    }

    public TongYiChatModel(TongYiAiApi tongYiAiApi, TongYiChatOptions options,
                           FunctionCallbackResolver functionCallbackResolver, List<FunctionCallback> toolFunctionCallbacks,
                           RetryTemplate retryTemplate) {
        this(tongYiAiApi, options, functionCallbackResolver, toolFunctionCallbacks, retryTemplate, ObservationRegistry.NOOP);
    }

    public TongYiChatModel(TongYiAiApi tongYiAiApi, TongYiChatOptions options,
                           FunctionCallbackResolver functionCallbackResolver, List<FunctionCallback> toolFunctionCallbacks,
                           RetryTemplate retryTemplate, ObservationRegistry observationRegistry) {
        super(functionCallbackResolver, options, toolFunctionCallbacks);

        Assert.notNull(tongYiAiApi, "TongYiAiApi must not be null");
        Assert.notNull(options, "Options must not be null");
        Assert.notNull(retryTemplate, "RetryTemplate must not be null");
        Assert.isTrue(CollectionUtils.isEmpty(options.getFunctionCallbacks()),
                "The default function callbacks must be set via the toolFunctionCallbacks constructor parameter");
        Assert.notNull(observationRegistry, "ObservationRegistry must not be null");

        this.tongYiAiApi = tongYiAiApi;
        this.defaultOptions = options;
        this.retryTemplate = retryTemplate;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        TongYiAiApi.ChatCompletionRequest request = createRequest(prompt, false);

        ChatModelObservationContext observationContext = ChatModelObservationContext.builder()
                .prompt(prompt)
                .provider(TongYiAiApiConstants.PROVIDER_NAME)
                .requestOptions(buildRequestOptions(request))
                .build();

        ChatResponse response = ChatModelObservationDocumentation.CHAT_MODEL_OPERATION
                .observation(this.observationConvention, DEFAULT_OBSERVATION_CONVENTION, () -> observationContext,
                        this.observationRegistry)
                .observe(() -> {

                    ResponseEntity<TongYiAiApi.ChatCompletion> completionEntity = this.retryTemplate
                            .execute(ctx -> this.tongYiAiApi.chatCompletionEntity(request, getAdditionalHttpHeaders(prompt)));

                    var chatCompletion = completionEntity.getBody();

                    if (chatCompletion == null) {
                        logger.warn("No chat completion returned for prompt: {}", prompt);
                        return new ChatResponse(List.of());
                    }

                    List<TongYiAiApi.ChatCompletion.Choice> choices = chatCompletion.choices();
                    if (choices == null) {
                        logger.warn("No choices returned for prompt: {}", prompt);
                        return new ChatResponse(List.of());
                    }

                    List<Generation> generations = choices.stream().map(choice -> {
                        // @formatter:off
                        Map<String, Object> metadata = Map.of(
                                "id", chatCompletion.id() != null ? chatCompletion.id() : "",
                                "role", choice.message().role() != null ? choice.message().role().name() : "",
                                "index", choice.index(),
                                "finishReason", choice.finishReason() != null ? choice.finishReason().name() : "",
                                "refusal", StringUtils.hasText(choice.message().refusal()) ? choice.message().refusal() : "");
                        // @formatter:on
                        return buildGeneration(choice, metadata, request);
                    }).toList();

                    // Non function calling.
                    RateLimit rateLimit = OpenAiResponseHeaderExtractor.extractAiResponseHeaders(completionEntity);

                    ChatResponse chatResponse = new ChatResponse(generations, from(completionEntity.getBody(), rateLimit));

                    observationContext.setResponse(chatResponse);

                    return chatResponse;

                });

        if (response != null && isToolCall(response, Set.of(TongYiAiApi.ChatCompletionFinishReason.TOOL_CALLS.name(),
                TongYiAiApi.ChatCompletionFinishReason.STOP.name()))) {
            var toolCallConversation = handleToolCalls(prompt, response);
            // Recursively call the call method with the tool call message
            // conversation that contains the call responses.
            return this.call(new Prompt(toolCallConversation, prompt.getOptions()));
        }

        return response;
    }

    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {
        return Flux.deferContextual(contextView -> {
            TongYiAiApi.ChatCompletionRequest request = createRequest(prompt, true);

            Flux<TongYiAiApi.ChatCompletionChunk> completionChunks = this.tongYiAiApi.chatCompletionStream(request,
                    getAdditionalHttpHeaders(prompt));

            // For chunked responses, only the first chunk contains the choice role.
            // The rest of the chunks with same ID share the same role.
            ConcurrentHashMap<String, String> roleMap = new ConcurrentHashMap<>();

            final ChatModelObservationContext observationContext = ChatModelObservationContext.builder()
                    .prompt(prompt)
                    .provider(TongYiAiApiConstants.PROVIDER_NAME)
                    .requestOptions(buildRequestOptions(request))
                    .build();

            Observation observation = ChatModelObservationDocumentation.CHAT_MODEL_OPERATION.observation(
                    this.observationConvention, DEFAULT_OBSERVATION_CONVENTION, () -> observationContext,
                    this.observationRegistry);

            observation.parentObservation(contextView.getOrDefault(ObservationThreadLocalAccessor.KEY, null)).start();

            // Convert the ChatCompletionChunk into a ChatCompletion to be able to reuse
            // the function call handling logic.
            Flux<ChatResponse> chatResponse = completionChunks.map(this::chunkToChatCompletion)
                    .switchMap(chatCompletion -> Mono.just(chatCompletion).map(chatCompletion2 -> {
                        try {
                            @SuppressWarnings("null")
                            String id = chatCompletion2.id();

                            List<Generation> generations = chatCompletion2.choices().stream().map(choice -> {// @formatter:off

                                if (choice.message().role() != null) {
                                    roleMap.putIfAbsent(id, choice.message().role().name());
                                }
                                Map<String, Object> metadata = Map.of(
                                        "id", chatCompletion2.id(),
                                        "role", roleMap.getOrDefault(id, ""),
                                        "index", choice.index(),
                                        "finishReason", choice.finishReason() != null ? choice.finishReason().name() : "",
                                        "refusal", StringUtils.hasText(choice.message().refusal()) ? choice.message().refusal() : "");

                                return buildGeneration(choice, metadata, request);
                            }).toList();
                            // @formatter:on

                            return new ChatResponse(generations, from(chatCompletion2, null));
                        }
                        catch (Exception e) {
                            logger.error("Error processing chat completion", e);
                            return new ChatResponse(List.of());
                        }

                    }));

            // @formatter:off
            Flux<ChatResponse> flux = chatResponse.flatMap(response -> {

                        if (isToolCall(response, Set.of(TongYiAiApi.ChatCompletionFinishReason.TOOL_CALLS.name(),
                                TongYiAiApi.ChatCompletionFinishReason.STOP.name()))) {
                            var toolCallConversation = handleToolCalls(prompt, response);
                            // Recursively call the stream method with the tool call message
                            // conversation that contains the call responses.
                            return this.stream(new Prompt(toolCallConversation, prompt.getOptions()));
                        }
                        else {
                            return Flux.just(response);
                        }
                    })
                    .doOnError(observation::error)
                    .doFinally(s -> {
                        // TODO: Consider a custom ObservationContext and
                        // include additional metadata
                        // if (s == SignalType.CANCEL) {
                        // observationContext.setAborted(true);
                        // }
                        observation.stop();
                    })
                    .contextWrite(ctx -> ctx.put(ObservationThreadLocalAccessor.KEY, observation));
            // @formatter:on

            return new MessageAggregator().aggregate(flux, observationContext::setResponse);

        }).doOnError(error -> {
            // 记录错误信息
            logger.error("Error encountered: " + error.getMessage());
        });
    }

    private MultiValueMap<String, String> getAdditionalHttpHeaders(Prompt prompt) {

        Map<String, String> headers = new HashMap<>(this.defaultOptions.getHttpHeaders());
        if (prompt.getOptions() != null && prompt.getOptions() instanceof TongYiChatOptions chatOptions) {
            headers.putAll(chatOptions.getHttpHeaders());
        }
        return CollectionUtils.toMultiValueMap(headers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> List.of(e.getValue()))));
    }

    private Generation buildGeneration(TongYiAiApi.ChatCompletion.Choice choice, Map<String, Object> metadata, TongYiAiApi.ChatCompletionRequest request) {
        List<AssistantMessage.ToolCall> toolCalls = choice.message().toolCalls() == null ? List.of()
                : choice.message()
                .toolCalls()
                .stream()
                .map(toolCall -> new AssistantMessage.ToolCall(toolCall.id(), "function",
                        toolCall.function().name(), toolCall.function().arguments()))
                .toList();

        String finishReason = (choice.finishReason() != null ? choice.finishReason().name() : "");
        var generationMetadataBuilder = ChatGenerationMetadata.builder().finishReason(finishReason);

        List<Media> media = new ArrayList<>();
        String textContent = choice.message().content();
        var audioOutput = choice.message().audioOutput();
        if (audioOutput != null) {
            String mimeType = String.format("audio/%s", request.audioParameters().format().name().toLowerCase());
            byte[] audioData = Base64.getDecoder().decode(audioOutput.data());
            Resource resource = new ByteArrayResource(audioData);
            media.add(new Media(MimeTypeUtils.parseMimeType(mimeType), resource, audioOutput.id()));
            if (!StringUtils.hasText(textContent)) {
                textContent = audioOutput.transcript();
            }
            generationMetadataBuilder.metadata("audioId", audioOutput.id());
            generationMetadataBuilder.metadata("audioExpiresAt", audioOutput.expiresAt());
        }

        var assistantMessage = new AssistantMessage(textContent, metadata, toolCalls, media);
        return new Generation(assistantMessage, generationMetadataBuilder.build());
    }

    private ChatResponseMetadata from(TongYiAiApi.ChatCompletion result, RateLimit rateLimit) {
        Assert.notNull(result, "TongYiAi ChatCompletionResult must not be null");
        var builder = ChatResponseMetadata.builder()
                .id(result.id() != null ? result.id() : "")
                .usage(result.usage() != null ? TongYiAiUsage.from(result.usage()) : new EmptyUsage())
                .model(result.model() != null ? result.model() : "")
                .keyValue("created", result.created() != null ? result.created() : 0L)
                .keyValue("system-fingerprint", result.systemFingerprint() != null ? result.systemFingerprint() : "");
        if (rateLimit != null) {
            builder.rateLimit(rateLimit);
        }
        return builder.build();
    }

    /**
     * Convert the ChatCompletionChunk into a ChatCompletion. The Usage is set to null.
     * @param chunk the ChatCompletionChunk to convert
     * @return the ChatCompletion
     */
    private TongYiAiApi.ChatCompletion chunkToChatCompletion(TongYiAiApi.ChatCompletionChunk chunk) {
        List<TongYiAiApi.ChatCompletion.Choice> choices = chunk.choices()
                .stream()
                .map(chunkChoice -> new TongYiAiApi.ChatCompletion.Choice(chunkChoice.finishReason(), chunkChoice.index(), chunkChoice.delta(),
                        chunkChoice.logprobs()))
                .toList();

        return new TongYiAiApi.ChatCompletion(chunk.id(), choices, chunk.created(), chunk.model(),
                chunk.systemFingerprint(), "chat.completion", chunk.usage());
    }

    TongYiAiApi.ChatCompletionRequest createRequest(Prompt prompt, boolean stream) {

        List<TongYiAiApi.ChatCompletionMessage> chatCompletionMessages = prompt.getInstructions().stream().map(message -> {
            if (message.getMessageType() == MessageType.USER || message.getMessageType() == MessageType.SYSTEM) {
                Object content = message.getContent();
                if (message instanceof UserMessage userMessage) {
                    if (!CollectionUtils.isEmpty(userMessage.getMedia())) {
                        List<TongYiAiApi.ChatCompletionMessage.MediaContent> contentList = new ArrayList<>(
                                List.of(new TongYiAiApi.ChatCompletionMessage.MediaContent(message.getContent())));

                        contentList.addAll(userMessage.getMedia()
                                .stream()
                                .map(media -> new TongYiAiApi.ChatCompletionMessage.MediaContent(new TongYiAiApi.ChatCompletionMessage.MediaContent.ImageUrl(
                                        this.fromMediaData(media.getMimeType(), media.getData()))))
                                .toList());

                        content = contentList;
                    }
                }

                return List.of(new TongYiAiApi.ChatCompletionMessage(content,
                        TongYiAiApi.ChatCompletionMessage.Role.valueOf(message.getMessageType().name())));
            }
            else if (message.getMessageType() == MessageType.ASSISTANT) {
                var assistantMessage = (AssistantMessage) message;
                List<TongYiAiApi.ChatCompletionMessage.ToolCall> toolCalls = null;
                if (!CollectionUtils.isEmpty(assistantMessage.getToolCalls())) {
                    toolCalls = assistantMessage.getToolCalls().stream().map(toolCall -> {
                        var function = new TongYiAiApi.ChatCompletionMessage.ChatCompletionFunction(toolCall.name(), toolCall.arguments());
                        return new TongYiAiApi.ChatCompletionMessage.ToolCall(toolCall.id(), toolCall.type(), function);
                    }).toList();
                }
                TongYiAiApi.ChatCompletionMessage.AudioOutput audioOutput = null;
                if (!CollectionUtils.isEmpty(assistantMessage.getMedia())) {
                    Assert.isTrue(assistantMessage.getMedia().size() == 1,
                            "Only one media content is supported for assistant messages");
                    audioOutput = new TongYiAiApi.ChatCompletionMessage.AudioOutput(assistantMessage.getMedia().get(0).getId(), null, null, null);
                }
                return List.of(new TongYiAiApi.ChatCompletionMessage(assistantMessage.getContent(),
                        TongYiAiApi.ChatCompletionMessage.Role.ASSISTANT, null, null, toolCalls, null, audioOutput));
            }
            else if (message.getMessageType() == MessageType.TOOL) {
                ToolResponseMessage toolMessage = (ToolResponseMessage) message;

                toolMessage.getResponses().forEach(response -> {
                    Assert.isTrue(response.id() != null, "ToolResponseMessage must have an id");
                    Assert.isTrue(response.name() != null, "ToolResponseMessage must have a name");
                });

                return toolMessage.getResponses()
                        .stream()
                        .map(tr -> new TongYiAiApi.ChatCompletionMessage(tr.responseData(), TongYiAiApi.ChatCompletionMessage.Role.TOOL, tr.name(),
                                tr.id(), null, null, null))
                        .toList();
            }
            else {
                throw new IllegalArgumentException("Unsupported message type: " + message.getMessageType());
            }
        }).flatMap(List::stream).toList();

        TongYiAiApi.ChatCompletionRequest request = new TongYiAiApi.ChatCompletionRequest(chatCompletionMessages, stream);

        Set<String> enabledToolsToUse = new HashSet<>();

        if (prompt.getOptions() != null) {
            TongYiChatOptions updatedRuntimeOptions = ModelOptionsUtils.copyToTarget(prompt.getOptions(), ChatOptions.class, TongYiChatOptions.class);

            enabledToolsToUse.addAll(this.runtimeFunctionCallbackConfigurations(updatedRuntimeOptions));

            request = ModelOptionsUtils.merge(updatedRuntimeOptions, request, TongYiAiApi.ChatCompletionRequest.class);
        }

        if (!CollectionUtils.isEmpty(this.defaultOptions.getFunctions())) {
            enabledToolsToUse.addAll(this.defaultOptions.getFunctions());
        }

        request = ModelOptionsUtils.merge(request, this.defaultOptions, TongYiAiApi.ChatCompletionRequest.class);

        // Add the enabled functions definitions to the request's tools parameter.
        if (!CollectionUtils.isEmpty(enabledToolsToUse)) {

            request = ModelOptionsUtils.merge(
                    TongYiChatOptions.builder().withTools(this.getFunctionTools(enabledToolsToUse)).build(), request,
                    TongYiAiApi.ChatCompletionRequest.class);
        }

        // Remove `streamOptions` from the request if it is not a streaming request
        if (request.streamOptions() != null && !stream) {
            logger.warn("Removing streamOptions from the request as it is not a streaming request!");
            request = request.withStreamOptions(null);
        }

        return request;
    }

    private String fromMediaData(MimeType mimeType, Object mediaContentData) {
        if (mediaContentData instanceof byte[] bytes) {
            // Assume the bytes are an image. So, convert the bytes to a base64 encoded
            // following the prefix pattern.
            return String.format("data:%s;base64,%s", mimeType.toString(), Base64.getEncoder().encodeToString(bytes));
        }
        else if (mediaContentData instanceof String text) {
            // Assume the text is a URLs or a base64 encoded image prefixed by the user.
            return text;
        }
        else {
            throw new IllegalArgumentException(
                    "Unsupported media data type: " + mediaContentData.getClass().getSimpleName());
        }
    }

    private List<TongYiAiApi.FunctionTool> getFunctionTools(Set<String> functionNames) {
        return this.resolveFunctionCallbacks(functionNames).stream().map(functionCallback -> {
            var function = new TongYiAiApi.FunctionTool.Function(functionCallback.getDescription(),
                    functionCallback.getName(), functionCallback.getInputTypeSchema());
            return new TongYiAiApi.FunctionTool(function);
        }).toList();
    }

    private ChatOptions buildRequestOptions(TongYiAiApi.ChatCompletionRequest request) {
        return ChatOptions.builder()
                .model(request.model())
                .maxTokens(request.maxTokens())
                .stopSequences(request.stop())
                .temperature(request.temperature())
                .topP(request.topP())
                .build();
    }

    @Override
    public ChatOptions getDefaultOptions() {
        return TongYiChatOptions.fromOptions(this.defaultOptions);
    }

    @Override
    public String toString() {
        return "TongYiChatModel [defaultOptions=" + defaultOptions + "]";
    }

}
