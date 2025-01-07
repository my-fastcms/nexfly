package com.nexfly.ai.tongyi.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.model.ChatModelDescription;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * Single class implementation of the TongYi Chat Completion API and Embedding API.
 * <a href="https://help.aliyun.com/zh/model-studio/developer-reference/use-qwen-by-calling-api">TongYi Docs</a>
 *
 * @author wangjun
 * @since 1.0
 */
public class TongYiAiApi {

    public static final String DEFAULT_CHAT_MODEL = ChatModel.QWEN_TURBO.getValue();

    public static final String DEFAULT_EMBEDDING_MODEL = EmbeddingModel.Embedding_2.getValue();

    private static final Predicate<String> SSE_DONE_PREDICATE = "[DONE]"::equals;

    private final RestClient restClient;

    private final WebClient webClient;

    private final String completionsPath = "/chat/completions";

    public TongYiAiApi(String apiKey) {
        this(TongYiAiApiConstants.DEFAULT_BASE_URL, apiKey);
    }

    public TongYiAiApi(String baseUrl, String apiKey) {
        this(baseUrl, apiKey, RestClient.builder(), WebClient.builder());
    }

    public TongYiAiApi(String baseUrl, String apiKey, RestClient.Builder restClientBuilder, WebClient.Builder webClientBuilder) {
        this(baseUrl, apiKey, restClientBuilder, webClientBuilder, RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
    }

    public TongYiAiApi(String baseUrl, String apiKey, RestClient.Builder restClientBuilder, WebClient.Builder webClientBuilder, ResponseErrorHandler responseErrorHandler) {
        this(baseUrl, apiKey, CollectionUtils.toMultiValueMap(Map.of()), restClientBuilder, webClientBuilder, responseErrorHandler);
    }

    public TongYiAiApi(String baseUrl, String apiKey, MultiValueMap<String, String> headers, RestClient.Builder restClientBuilder, WebClient.Builder webClientBuilder, ResponseErrorHandler responseErrorHandler) {
        Assert.notNull(headers, "Headers must not be null");
        // @formatter:off
        this.restClient = restClientBuilder.baseUrl(baseUrl)
                .defaultHeaders(h -> {
                    h.setBearerAuth(apiKey);
                    h.setContentType(MediaType.APPLICATION_JSON);
                    h.addAll(headers);
                })
                .defaultStatusHandler(responseErrorHandler)
                .build();

        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeaders(h -> {
                    h.setBearerAuth(apiKey);
                    h.setContentType(MediaType.APPLICATION_JSON);
                    h.addAll(headers);
                })
                .build();// @formatter:on
    }

    public enum ChatModel implements ChatModelDescription {

        QWEN_TURBO("qwen-turbo"),

        BAILIAN_V1("bailian-v1"),

        DOLLY_12B_V2("dolly-12b-v2"),

        QWEN_PLUS("qwen-plus"),

        QWEN_MAX("qwen-max");

        public final String value;

        ChatModel(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        @Override
        public String getName() {
            return this.value;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FunctionTool(
            @JsonProperty("type") FunctionTool.Type type,
            @JsonProperty("function") FunctionTool.Function function) {

        /**
         * Create a tool of type 'function' and the given function definition.
         * @param function function definition.
         */
        @ConstructorBinding
        public FunctionTool(FunctionTool.Function function) {
            this(FunctionTool.Type.FUNCTION, function);
        }

        /**
         * Create a tool of type 'function' and the given function definition.
         */
        public enum Type {
            /**
             * Function tool type.
             */
            @JsonProperty("function") FUNCTION
        }

        /**
         * Function definition.
         *
         * @param description A description of what the function does, used by the model to choose when and how to call
         * the function.
         * @param name The name of the function to be called. Must be a-z, A-Z, 0-9, or contain underscores and dashes,
         * with a maximum length of 64.
         * @param parameters The parameters the functions accepts, described as a JSON Schema object. To describe a
         * function that accepts no parameters, provide the value {"type": "object", "properties": {}}.
         */
        public record Function(
                @JsonProperty("description") String description,
                @JsonProperty("name") String name,
                @JsonProperty("parameters") Map<String, Object> parameters) {

            /**
             * Create tool function definition.
             *
             * @param description tool function description.
             * @param name tool function name.
             * @param jsonSchema tool function schema as json.
             */
            @ConstructorBinding
            public Function(String description, String name, String jsonSchema) {
                this(description, name, ModelOptionsUtils.jsonToMap(jsonSchema));
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatCompletionRequest (
            @JsonProperty("messages") List<ChatCompletionMessage> messages,
            @JsonProperty("model") String model,
            @JsonProperty("max_tokens") Integer maxTokens,
            @JsonProperty("seed") Integer seed,
            @JsonProperty("stop") List<String> stop,
            @JsonProperty("stream") Boolean stream,
            @JsonProperty("stream_options") StreamOptions streamOptions,
            @JsonProperty("temperature") Double temperature,
            @JsonProperty("presence_penalty") Float presencePenalty,
            @JsonProperty("top_p") Double topP,
            @JsonProperty("tools") List<FunctionTool> tools,
            @JsonProperty("tool_choice") Object toolChoice,
            @JsonProperty("user") String user,
            @JsonProperty("enable_search") Boolean enableSearch,
            @JsonProperty("audio") AudioParameters audioParameters) {

        /**
         * Shortcut constructor for a chat completion request with the given messages and model.
         *
         * @param messages A list of messages comprising the conversation so far.
         * @param model ID of the model to use.
         * @param temperature What sampling temperature to use, between 0 and 1.
         */
        public ChatCompletionRequest(List<ChatCompletionMessage> messages, String model, Double temperature) {
            this(messages, model, null,  null, null, false, null, temperature, null, null, null, null, null, null, null);
        }

        /**
         * Shortcut constructor for a chat completion request with the given messages, model and control for streaming.
         *
         * @param messages A list of messages comprising the conversation so far.
         * @param model ID of the model to use.
         * @param temperature What sampling temperature to use, between 0 and 1.
         * @param stream If set, partial message deltas will be sent.Tokens will be sent as data-only server-sent events
         * as they become available, with the stream terminated by a data: [DONE] message.
         */
        public ChatCompletionRequest(List<ChatCompletionMessage> messages, String model, Double temperature, boolean stream) {
            this(messages, model, null,  null, null, stream, null, temperature, null,
                    null, null, null, null, null, null);
        }

        /**
         * Shortcut constructor for a chat completion request with the given messages, model, tools and tool choice.
         * Streaming is set to false, temperature to 0.8 and all other parameters are null.
         *
         * @param messages A list of messages comprising the conversation so far.
         * @param model ID of the model to use.
         * @param tools A list of tools the model may call. Currently, only functions are supported as a tool.
         * @param toolChoice Controls which (if any) function is called by the model.
         */
        public ChatCompletionRequest(List<ChatCompletionMessage> messages, String model,
                                     List<FunctionTool> tools, Object toolChoice) {
            this(messages, model, null, null, null, false, null, 0.8d, null, null, tools, toolChoice, null, null, null);
        }

        /**
         * Shortcut constructor for a chat completion request with the given messages, model, tools and tool choice.
         * Streaming is set to false, temperature to 0.8 and all other parameters are null.
         *
         * @param messages A list of messages comprising the conversation so far.
         * @param stream If set, partial message deltas will be sent.Tokens will be sent as data-only server-sent events
         * as they become available, with the stream terminated by a data: [DONE] message.
         */
        public ChatCompletionRequest(List<ChatCompletionMessage> messages, Boolean stream) {
            this(messages, null, null,  null, null,  stream, null, null,  null, null, null, null, null, null, null);
        }

        public ChatCompletionRequest withStreamOptions(ChatCompletionRequest.StreamOptions streamOptions) {
            return new ChatCompletionRequest(messages, model, maxTokens, seed, stop, stream, streamOptions, temperature, presencePenalty, topP,
                    tools, toolChoice, user, enableSearch, null);
        }

        /**
         * Helper factory that creates a tool_choice of type 'none', 'auto' or selected function by name.
         */
        public static class ToolChoiceBuilder {
            /**
             * Model can pick between generating a message or calling a function.
             */
            public static final String AUTO = "auto";
            /**
             * Model will not call a function and instead generates a message
             */
            public static final String NONE = "none";

            /**
             * Specifying a particular function forces the model to call that function.
             */
            public static Object function(String functionName) {
                return Map.of("type", "function", "function", Map.of("name", functionName));
            }
        }

        /**
         * Parameters for audio output. Required when audio output is requested with outputModalities: ["audio"].
         * @param voice Specifies the voice type.
         * @param format Specifies the output audio format.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record AudioParameters(
                @JsonProperty("voice") AudioParameters.Voice voice,
                @JsonProperty("format") AudioParameters.AudioResponseFormat format) {

            /**
             * Specifies the voice type.
             */
            public enum Voice {
                /** Alloy voice */
                @JsonProperty("alloy") ALLOY,
                /** Echo voice */
                @JsonProperty("echo") ECHO,
                /** Fable voice */
                @JsonProperty("fable") FABLE,
                /** Onyx voice */
                @JsonProperty("onyx") ONYX,
                /** Nova voice */
                @JsonProperty("nova") NOVA,
                /** Shimmer voice */
                @JsonProperty("shimmer") SHIMMER
            }

            /**
             * Specifies the output audio format.
             */
            public enum AudioResponseFormat {
                /** MP3 format */
                @JsonProperty("mp3") MP3,
                /** FLAC format */
                @JsonProperty("flac") FLAC,
                /** OPUS format */
                @JsonProperty("opus") OPUS,
                /** PCM16 format */
                @JsonProperty("pcm16") PCM16,
                /** WAV format */
                @JsonProperty("wav") WAV
            }
        }

        /**
         * An object specifying the format that the model must output.
         * @param type Must be one of 'text' or 'json_object'.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record ResponseFormat(
                @JsonProperty("type") String type) {
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record StreamOptions(
                @JsonProperty("include_usage") Boolean includeUsage) {

            public static ChatCompletionRequest.StreamOptions INCLUDE_USAGE = new ChatCompletionRequest.StreamOptions(true);
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatCompletionMessage(
            @JsonProperty("content") Object rawContent,
            @JsonProperty("role") ChatCompletionMessage.Role role,
            @JsonProperty("name") String name,
            @JsonProperty("tool_call_id") String toolCallId,
            @JsonProperty("tool_calls")
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY) List<ChatCompletionMessage.ToolCall> toolCalls,
            @JsonProperty("refusal") String refusal,
            @JsonProperty("audio") AudioOutput audioOutput) {

        /**
         * Get message content as String.
         */
        public String content() {
            if (this.rawContent == null) {
                return null;
            }
            if (this.rawContent instanceof String text) {
                return text;
            }
            throw new IllegalStateException("The content is not a string!");
        }

        /**
         * Create a chat completion message with the given content and role. All other fields are null.
         * @param content The contents of the message.
         * @param role The role of the author of this message.
         */
        public ChatCompletionMessage(Object content, Role role) {
            this(content, role, null, null, null, null, null);
        }

        /**
         * The role of the author of this message.
         */
        public enum Role {
            /**
             * System message.
             */
            @JsonProperty("system") SYSTEM,
            /**
             * User message.
             */
            @JsonProperty("user") USER,
            /**
             * Assistant message.
             */
            @JsonProperty("assistant") ASSISTANT,
            /**
             * Tool message.
             */
            @JsonProperty("tool") TOOL
        }

        /**
         * An array of content parts with a defined type.
         * Each MediaContent can be of either "text" or "image_url" type. Not both.
         *
         * @param type Content  type, each can be of type text or image_url.
         * @param text The text content of the message.
         * @param imageUrl The image content of the message. You can pass multiple
         * images by adding multiple image_url content parts. Image input is only
         * supported when using the glm-4v model.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record MediaContent(
                @JsonProperty("type") String type,
                @JsonProperty("text") String text,
                @JsonProperty("image_url") ImageUrl imageUrl,
                @JsonProperty("input_audio") InputAudio inputAudio) {

            /**
             * Shortcut constructor for a text content.
             * @param text The text content of the message.
             */
            public MediaContent(String text) {
                this("text", text, null, null);
            }

            /**
             * Shortcut constructor for an image content.
             * @param imageUrl The image content of the message.
             */
            public MediaContent(ImageUrl imageUrl) {
                this("image_url", null, imageUrl, null);
            }

            /**
             * Shortcut constructor for an audio content.
             * @param inputAudio The audio content of the message.
             */
            public MediaContent(InputAudio inputAudio) {
                this("input_audio", null, null, inputAudio);
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            public record InputAudio(// @formatter:off
                                     @JsonProperty("data") String data,
                                     @JsonProperty("format") InputAudio.Format format) {

                public enum Format {
                    /** MP3 audio format */
                    @JsonProperty("mp3") MP3,
                    /** WAV audio format */
                    @JsonProperty("wav") WAV
                } // @formatter:on
            }

            /**
             * Shortcut constructor for an image content.
             *
             * @param url Either a URL of the image or the base64 encoded image data. The
             * base64 encoded image data must have a special prefix in the following
             * format: "data:{mimetype};base64,{base64-encoded-image-data}".
             * @param detail Specifies the detail level of the image.
             */
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public record ImageUrl(@JsonProperty("url") String url, @JsonProperty("detail") String detail) {

                public ImageUrl(String url) {
                    this(url, null);
                }

            }

        }
        /**
         * The relevant tool call.
         *
         * @param id The ID of the tool call. This ID must be referenced when you submit the tool outputs in using the
         * Submit tool outputs to run endpoint.
         * @param type The type of tool call the output is required for. For now, this is always function.
         * @param function The function definition.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record ToolCall(
                @JsonProperty("id") String id,
                @JsonProperty("type") String type,
                @JsonProperty("function") ChatCompletionMessage.ChatCompletionFunction function) {
        }

        /**
         * The function definition.
         *
         * @param name The name of the function.
         * @param arguments The arguments that the model expects you to pass to the function.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record ChatCompletionFunction(
                @JsonProperty("name") String name,
                @JsonProperty("arguments") String arguments) {
        }

        /**
         * Audio response from the model.
         *
         * @param id Unique identifier for the audio response from the model.
         * @param data Audio output from the model.
         * @param expiresAt When the audio content will no longer be available on the
         * server.
         * @param transcript Transcript of the audio output from the model.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record AudioOutput(// @formatter:off
                                  @JsonProperty("id") String id,
                                  @JsonProperty("data") String data,
                                  @JsonProperty("expires_at") Long expiresAt,
                                  @JsonProperty("transcript") String transcript
        ) { // @formatter:on
        }

    }

    public static String getTextContent(List<ChatCompletionMessage.MediaContent> content) {
        return content.stream()
                .filter(c -> "text".equals(c.type()))
                .map(ChatCompletionMessage.MediaContent::text)
                .reduce("", (a, b) -> a + b);
    }

    /**
     * The reason the model stopped generating tokens.
     */
    public enum ChatCompletionFinishReason {
        /**
         * The model hit a natural stop point or a provided stop sequence.
         */
        @JsonProperty("stop") STOP,
        /**
         * The maximum number of tokens specified in the request was reached.
         */
        @JsonProperty("length") LENGTH,
        /**
         * The content was omitted due to a flag from our content filters.
         */
        @JsonProperty("content_filter") CONTENT_FILTER,
        /**
         * The model called a tool.
         */
        @JsonProperty("tool_calls") TOOL_CALLS,
        /**
         * (deprecated) The model called a function.
         */
        @JsonProperty("function_call") FUNCTION_CALL,
        /**
         * Only for compatibility with Mistral AI API.
         */
        @JsonProperty("tool_call") TOOL_CALL
    }

    /**
     * Represents a chat completion response returned by model, based on the provided input.
     *
     * @param id A unique identifier for the chat completion.
     * @param choices A list of chat completion choices. Can be more than one if n is greater than 1.
     * @param created The Unix timestamp (in seconds) of when the chat completion was created.
     * @param model The model used for the chat completion.
     * @param systemFingerprint This fingerprint represents the backend configuration that the model runs with. Can be
     * used in conjunction with the seed request parameter to understand when backend changes have been made that might
     * impact determinism.
     * @param object The object type, which is always chat.completion.
     * @param usage Usage statistics for the completion request.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatCompletion(
            @JsonProperty("id") String id,
            @JsonProperty("choices") List<ChatCompletion.Choice> choices,
            @JsonProperty("created") Long created,
            @JsonProperty("model") String model,
            @JsonProperty("system_fingerprint") String systemFingerprint,
            @JsonProperty("object") String object,
            @JsonProperty("usage") Usage usage) {

        /**
         * Chat completion choice.
         *
         * @param finishReason The reason the model stopped generating tokens.
         * @param index The index of the choice in the list of choices.
         * @param message A chat completion message generated by the model.
         * @param logprobs Log probability information for the choice.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record Choice(
                @JsonProperty("finish_reason") ChatCompletionFinishReason finishReason,
                @JsonProperty("index") Integer index,
                @JsonProperty("message") ChatCompletionMessage message,
                @JsonProperty("logprobs") LogProbs logprobs) {

        }
    }

    /**
     * Log probability information for the choice.
     *
     * @param content A list of message content tokens with log probability information.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record LogProbs(
            @JsonProperty("content") List<LogProbs.Content> content) {

        /**
         * Message content tokens with log probability information.
         *
         * @param token The token.
         * @param logprob The log probability of the token.
         * @param probBytes A list of integers representing the UTF-8 bytes representation
         * of the token. Useful in instances where characters are represented by multiple
         * tokens and their byte representations must be combined to generate the correct
         * text representation. Can be null if there is no bytes representation for the token.
         * @param topLogprobs List of the most likely tokens and their log probability,
         * at this token position. In rare cases, there may be fewer than the number of
         * requested top_logprobs returned.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record Content(
                @JsonProperty("token") String token,
                @JsonProperty("logprob") Float logprob,
                @JsonProperty("bytes") List<Integer> probBytes,
                @JsonProperty("top_logprobs") List<LogProbs.Content.TopLogProbs> topLogprobs) {

            /**
             * The most likely tokens and their log probability, at this token position.
             *
             * @param token The token.
             * @param logprob The log probability of the token.
             * @param probBytes A list of integers representing the UTF-8 bytes representation
             * of the token. Useful in instances where characters are represented by multiple
             * tokens and their byte representations must be combined to generate the correct
             * text representation. Can be null if there is no bytes representation for the token.
             */
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public record TopLogProbs(
                    @JsonProperty("token") String token,
                    @JsonProperty("logprob") Float logprob,
                    @JsonProperty("bytes") List<Integer> probBytes) {
            }
        }
    }

    /**
     * Usage statistics for the completion request.
     *
     * @param completionTokens Number of tokens in the generated completion. Only applicable for completion requests.
     * @param promptTokens Number of tokens in the prompt.
     * @param totalTokens Total number of tokens used in the request (prompt + completion).
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Usage(
            @JsonProperty("completion_tokens") Integer completionTokens,
            @JsonProperty("prompt_tokens") Integer promptTokens,
            @JsonProperty("total_tokens") Integer totalTokens) {

    }

    /**
     * Represents a streamed chunk of a chat completion response returned by model, based on the provided input.
     *
     * @param id A unique identifier for the chat completion. Each chunk has the same ID.
     * @param choices A list of chat completion choices. Can be more than one if n is greater than 1.
     * @param created The Unix timestamp (in seconds) of when the chat completion was created. Each chunk has the same
     * timestamp.
     * @param model The model used for the chat completion.
     * @param systemFingerprint This fingerprint represents the backend configuration that the model runs with. Can be
     * used in conjunction with the seed request parameter to understand when backend changes have been made that might
     * impact determinism.
     * @param object The object type, which is always 'chat.completion.chunk'.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatCompletionChunk(
            @JsonProperty("id") String id,
            @JsonProperty("choices") List<ChatCompletionChunk.ChunkChoice> choices,
            @JsonProperty("created") Long created,
            @JsonProperty("model") String model,
            @JsonProperty("system_fingerprint") String systemFingerprint,
            @JsonProperty("object") String object,
            @JsonProperty("usage") Usage usage) {

        /**
         * Chat completion choice.
         *
         * @param finishReason The reason the model stopped generating tokens.
         * @param index The index of the choice in the list of choices.
         * @param delta A chat completion delta generated by streamed model responses.
         * @param logprobs Log probability information for the choice.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record ChunkChoice(
                @JsonProperty("finish_reason") ChatCompletionFinishReason finishReason,
                @JsonProperty("index") Integer index,
                @JsonProperty("delta") ChatCompletionMessage delta,
                @JsonProperty("logprobs") LogProbs logprobs) {
        }
    }

    /**
     * Creates a model response for the given chat conversation.
     *
     * @param chatRequest The chat completion request.
     * @return Entity response with {@link ChatCompletion} as a body and HTTP status code and headers.
     */
    public ResponseEntity<ChatCompletion> chatCompletionEntity(ChatCompletionRequest chatRequest) {

        Assert.notNull(chatRequest, "The request body can not be null.");
        Assert.isTrue(!chatRequest.stream(), "Request must set the stream property to false.");

        return this.restClient.post()
                .uri(completionsPath)
                .body(chatRequest)
                .retrieve()
                .toEntity(ChatCompletion.class);
    }

    public ResponseEntity<ChatCompletion> chatCompletionEntity(ChatCompletionRequest chatRequest, MultiValueMap<String, String> additionalHttpHeader) {

        Assert.notNull(chatRequest, "The request body can not be null.");
        Assert.isTrue(!chatRequest.stream(), "Request must set the stream property to false.");
        Assert.notNull(additionalHttpHeader, "The additional HTTP headers can not be null.");

        return this.restClient.post()
                .uri(completionsPath)
                .headers(headers -> headers.addAll(additionalHttpHeader))
                .body(chatRequest)
                .retrieve()
                .toEntity(ChatCompletion.class);
    }

    private final TongYiStreamFunctionCallingHelper chunkMerger = new TongYiStreamFunctionCallingHelper();

    public Flux<ChatCompletionChunk> chatCompletionStream(ChatCompletionRequest chatRequest) {
        return chatCompletionStream(chatRequest, new LinkedMultiValueMap<>());
    }

    /**
     * Creates a streaming chat response for the given chat conversation.
     * @param chatRequest The chat completion request. Must have the stream property set
     * to true.
     * @param additionalHttpHeader Optional, additional HTTP headers to be added to the
     * request.
     * @return Returns a {@link Flux} stream from chat completion chunks.
     */
    public Flux<ChatCompletionChunk> chatCompletionStream(ChatCompletionRequest chatRequest,
                                                                    MultiValueMap<String, String> additionalHttpHeader) {

        Assert.notNull(chatRequest, "The request body can not be null.");
        Assert.isTrue(chatRequest.stream(), "Request must set the stream property to true.");

        AtomicBoolean isInsideTool = new AtomicBoolean(false);

        return this.webClient.post()
                .uri(completionsPath)
                .headers(headers -> headers.addAll(additionalHttpHeader))
                .body(Mono.just(chatRequest), ChatCompletionRequest.class)
                .retrieve()
                .bodyToFlux(String.class)
                // cancels the flux stream after the "[DONE]" is received.
                .takeUntil(SSE_DONE_PREDICATE)
                // filters out the "[DONE]" message.
                .filter(SSE_DONE_PREDICATE.negate())
                .map(content -> ModelOptionsUtils.jsonToObject(content, ChatCompletionChunk.class))
                // Detect is the chunk is part of a streaming function call.
                .map(chunk -> {
                    if (this.chunkMerger.isStreamingToolFunctionCall(chunk)) {
                        isInsideTool.set(true);
                    }
                    return chunk;
                })
                // Group all chunks belonging to the same function call.
                // Flux<ChatCompletionChunk> -> Flux<Flux<ChatCompletionChunk>>
                .windowUntil(chunk -> {
                    if (isInsideTool.get() && this.chunkMerger.isStreamingToolFunctionCallFinish(chunk)) {
                        isInsideTool.set(false);
                        return true;
                    }
                    return !isInsideTool.get();
                })
                // Merging the window chunks into a single chunk.
                // Reduce the inner Flux<ChatCompletionChunk> window into a single
                // Mono<ChatCompletionChunk>,
                // Flux<Flux<ChatCompletionChunk>> -> Flux<Mono<ChatCompletionChunk>>
                .concatMapIterable(window -> {
                    Mono<ChatCompletionChunk> monoChunk = window.reduce(
                            new ChatCompletionChunk(null, null, null, null, null, null, null),
                            (previous, current) -> this.chunkMerger.merge(previous, current));
                    return List.of(monoChunk);
                })
                // Flux<Mono<ChatCompletionChunk>> -> Flux<ChatCompletionChunk>
                .flatMap(mono -> mono);
    }

    public enum EmbeddingModel {

        Embedding_1("text-embedding-v1"),

        Embedding_2("text-embedding-v2");

        public final String  value;

        EmbeddingModel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Represents an embedding vector returned by embedding endpoint.
     *
     * @param index The index of the embedding in the list of embeddings.
     * @param embedding The embedding vector, which is a list of floats. The length of
     * vector depends on the model.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Embedding(// @formatter:off
                            @JsonProperty("text_index") Integer index,
                            @JsonProperty("embedding") float[] embedding) {// @formatter:on

    }

    /**
     * Creates an embedding vector representing the input text.
     *
     * @param input text to embed, encoded as a string or array of tokens. To embed
     * multiple inputs in a single request, pass an array of strings or array of token
     * arrays. The input must not exceed the max input tokens for the model (8192 tokens
     * for text-embedding-ada-002), cannot be an empty string, and any array must be 2048
     * dimensions or less.
     * @param model ID of the model to use.
     * @param parameters The parameters
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record EmbeddingRequest<T>(// @formatter:off
                                      @JsonProperty("input") Input input,
                                      @JsonProperty("model") String model,
                                      @JsonProperty("parameters") Parameters parameters) {// @formatter:on

        /**
         * Create an embedding request with the given input, model and encoding format set
         * to float.
         * @param input Input text to embed.
         * @param model ID of the model to use.
         */
        public EmbeddingRequest(Input input, String model) {
            this(input, model, null);
        }

        /**
         * Create an embedding request with the given input. Encoding format is set to
         * float and user is null and the model is set to 'text-embedding-ada-002'.
         * @param input Input text to embed.
         */
        public EmbeddingRequest(Input input) {
            this(input, DEFAULT_EMBEDDING_MODEL);
        }

        public record Input(@JsonProperty("texts") List<String> texts) {

        }
        public record Parameters(@JsonProperty("text_type") String textType) {

        }

    }

    /**
     * List of multiple embedding responses.
     *
     * @param <T> Type of the entities in the data list.
     * @param output List of entities.
     * @param model ID of the model to use.
     * @param usage Usage statistics for the completion request.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record EmbeddingList<T>(// @formatter:off
                                   @JsonProperty("output") Output output,
                                   @JsonProperty("model") String model,
                                   @JsonProperty("usage") Usage usage) {// @formatter:on

        public record Output(@JsonProperty("embeddings") List<Embedding> embeddings) {

        }
    }

    /**
     * Creates an embedding vector representing the input text or token array.
     * @param embeddingRequest The embedding request.
     * @return Returns list of {@link OpenAiApi.Embedding} wrapped in {@link OpenAiApi.EmbeddingList}.
     * @param <T> Type of the entity in the data list. Can be a {@link String} or
     * {@link List} of tokens (e.g. Integers). For embedding multiple inputs in a single
     * request, You can pass a {@link List} of {@link String} or {@link List} of
     * {@link List} of tokens. For example:
     *
     * <pre>{@code List.of("text1", "text2", "text3") or List.of(List.of(1, 2, 3), List.of(3, 4, 5))} </pre>
     */
    public <T> ResponseEntity<EmbeddingList<Embedding>> embeddings(EmbeddingRequest<T> embeddingRequest) {

        Assert.notNull(embeddingRequest, "The request body can not be null.");

        // Input text to embed, encoded as a string or array of tokens. To embed multiple
        // inputs in a single
        // request, pass an array of strings or array of token arrays.
        Assert.notNull(embeddingRequest.input(), "The input can not be null.");

        return this.restClient.post()
                .uri("/services/embeddings/text-embedding/text-embedding")
                .body(embeddingRequest)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });
    }

}
