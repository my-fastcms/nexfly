package com.nexfly.ai.tongyi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class TongYiAiImageApi {

	public static final String DEFAULT_IMAGE_MODEL = ImageModel.WANX_V1.getValue();

	private final RestClient restClient;

	/**
	 * Create a new TongYi Image api with base URL set to {@code https://api.openai.com}.
	 * @param tongYiToken TongYi apiKey.
	 */
	public TongYiAiImageApi(String tongYiToken) {
		this(TongYiAiApiConstants.DEFAULT_API_BASE_URL, tongYiToken, RestClient.builder());
	}

	/**
	 * Create a new TongYi Image API with the provided base URL.
	 * @param baseUrl the base URL for the TongYi API.
	 * @param tongYiToken TongYi apiKey.
	 * @param restClientBuilder the rest client builder to use.
	 */
	public TongYiAiImageApi(String baseUrl, String tongYiToken, RestClient.Builder restClientBuilder) {
		this(baseUrl, tongYiToken, restClientBuilder, RetryUtils.DEFAULT_RESPONSE_ERROR_HANDLER);
	}

	/**
	 * Create a new TongYi Image API with the provided base URL.
	 * @param baseUrl the base URL for the TongYi API.
	 * @param apiKey TongYi apiKey.
	 * @param restClientBuilder the rest client builder to use.
	 * @param responseErrorHandler the response error handler to use.
	 */
	public TongYiAiImageApi(String baseUrl, String apiKey, RestClient.Builder restClientBuilder,
							ResponseErrorHandler responseErrorHandler) {
		this(baseUrl, apiKey, CollectionUtils.toMultiValueMap(Map.of()), restClientBuilder, responseErrorHandler);
	}

	/**
	 * Create a new TongYi Image API with the provided base URL.
	 * @param baseUrl the base URL for the TongYi API.
	 * @param apiKey TongYi apiKey.
	 * @param headers the http headers to use.
	 * @param restClientBuilder the rest client builder to use.
	 * @param responseErrorHandler the response error handler to use.
	 */
	public TongYiAiImageApi(String baseUrl, String apiKey, MultiValueMap<String, String> headers,
							RestClient.Builder restClientBuilder, ResponseErrorHandler responseErrorHandler) {

		// @formatter:off
		this.restClient = restClientBuilder.baseUrl(baseUrl)
			.defaultHeaders(h -> {
				h.setBearerAuth(apiKey);
				h.setContentType(MediaType.APPLICATION_JSON);
				h.addAll(headers);
			})
			.defaultStatusHandler(responseErrorHandler)
			.build();
		// @formatter:on
	}

	/**
	 * TongYi Image API model.
	 * <a href="https://help.aliyun.com/zh/dashscope/developer-reference/api-details-9">DALL·E</a>
	 */
	public enum ImageModel {

		WANX_V1("wanx-v1"),

		WANX_SKETCH_TO_IMAGE_V1("wanx-sketch-to-image-v1");

		private final String value;

		ImageModel(String model) {
			this.value = model;
		}

		public String getValue() {
			return this.value;
		}

	}

	// @formatter:off
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record TongYiImageRequest (
		@JsonProperty("input") Input input,
		@JsonProperty("model") String model,
		@JsonProperty("parameters") Parameters parameters) {

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record Input(@JsonProperty("prompt") String prompt,
						@JsonProperty("negative_prompt") String negativePrompt,
						@JsonProperty("ref_img") String refImg) {
		public Input(String prompt) {
			this(prompt, "", "");
		}

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record Parameters(@JsonProperty("n") Integer n,
							 @JsonProperty("ref_strength") String refStrength,
							 @JsonProperty("ref_mode") String refMode,
							 @JsonProperty("seed") Integer seed,
							 @JsonProperty("size") String size,
							 @JsonProperty("style") String style) {

		public Parameters(Integer n) {
			this(n, "", "", 0, "1024*1024", "<auto>");
		}

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record TongYiImageResponse(
		@JsonProperty("request_id") String requestId,
		@JsonProperty("output") Data data) {
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record Data(
		@JsonProperty("task_id") String taskId,
		@JsonProperty("task_status") String taskStatus) {
	}
	// @formatter:onn

	public ResponseEntity<TongYiImageResponse> createImage(TongYiImageRequest tongYiImageRequest) {
		Assert.notNull(tongYiImageRequest, "Image request cannot be null.");
		Assert.hasLength(tongYiImageRequest.input().prompt, "Prompt cannot be empty.");

		return this.restClient.post()
			.uri("/services/aigc/text2image/image-synthesis")
			.header("X-DashScope-Async", "enable")
			.body(tongYiImageRequest)
			.retrieve()
			.toEntity(TongYiImageResponse.class);
	}

}
