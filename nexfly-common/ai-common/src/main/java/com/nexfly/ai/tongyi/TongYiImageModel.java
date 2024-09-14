package com.nexfly.ai.tongyi;

import com.nexfly.ai.tongyi.api.TongYiAiApiConstants;
import com.nexfly.ai.tongyi.api.TongYiAiImageApi;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.image.observation.DefaultImageModelObservationConvention;
import org.springframework.ai.image.observation.ImageModelObservationContext;
import org.springframework.ai.image.observation.ImageModelObservationConvention;
import org.springframework.ai.image.observation.ImageModelObservationDocumentation;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;

import java.util.List;

public class TongYiImageModel implements ImageModel {

	private final static Logger logger = LoggerFactory.getLogger(TongYiImageModel.class);

	private static final ImageModelObservationConvention DEFAULT_OBSERVATION_CONVENTION = new DefaultImageModelObservationConvention();

	/**
	 * The default options used for the image completion requests.
	 */
	private final TongYiImageOptions defaultOptions;

	/**
	 * The retry template used to retry the TongYiAI Image API calls.
	 */
	private final RetryTemplate retryTemplate;

	/**
	 * Low-level access to the TongYiAI Image API.
	 */
	private final TongYiAiImageApi tongYiAiImageApi;

	/**
	 * Observation registry used for instrumentation.
	 */
	private final ObservationRegistry observationRegistry;

	/**
	 * Conventions to use for generating observations.
	 */
	private ImageModelObservationConvention observationConvention = DEFAULT_OBSERVATION_CONVENTION;


	public TongYiImageModel(TongYiAiImageApi tongYiAiImageApi) {
		this(tongYiAiImageApi, TongYiImageOptions.builder().build());
	}

	/**
	 * Creates an instance of the OpenAiImageModel.
	 * @param tongYiAiImageApi The TongYiAiImageApi instance to be used for interacting with
	 * the TongYiAI Image API.
	 * @throws IllegalArgumentException if tongYiAiImageApi is null
	 */
	public TongYiImageModel(TongYiAiImageApi tongYiAiImageApi, TongYiImageOptions tongYiImageOptions) {
		this(tongYiAiImageApi, tongYiImageOptions, RetryUtils.DEFAULT_RETRY_TEMPLATE);
	}

	/**
	 * Initializes a new instance of the OpenAiImageModel.
	 * @param tongYiAiImageApi The TongYiAiImageApi instance to be used for interacting with
	 * the TongYiAI Image API.
	 * @param options The TongYiImageOptions to configure the image model.
	 * @param retryTemplate The retry template.
	 */
	public TongYiImageModel(TongYiAiImageApi tongYiAiImageApi, TongYiImageOptions options, RetryTemplate retryTemplate) {
		this(tongYiAiImageApi, options, retryTemplate, ObservationRegistry.NOOP);
	}

	/**
	 * Initializes a new instance of the OpenAiImageModel.
	 * @param tongYiAiImageApi The TongYiAiImageApi instance to be used for interacting with
	 * the TongYiAI Image API.
	 * @param options The TongYiImageOptions to configure the image model.
	 * @param retryTemplate The retry template.
	 * @param observationRegistry The ObservationRegistry used for instrumentation.
	 */
	public TongYiImageModel(TongYiAiImageApi tongYiAiImageApi, TongYiImageOptions options, RetryTemplate retryTemplate,
							ObservationRegistry observationRegistry) {
		Assert.notNull(tongYiAiImageApi, "TongYiAiImageApi must not be null");
		Assert.notNull(options, "options must not be null");
		Assert.notNull(retryTemplate, "retryTemplate must not be null");
		Assert.notNull(observationRegistry, "observationRegistry must not be null");
		this.tongYiAiImageApi = tongYiAiImageApi;
		this.defaultOptions = options;
		this.retryTemplate = retryTemplate;
		this.observationRegistry = observationRegistry;
	}

	@Override
	public ImageResponse call(ImagePrompt imagePrompt) {
		TongYiImageOptions requestImageOptions = mergeOptions(imagePrompt.getOptions(), this.defaultOptions);
		TongYiAiImageApi.TongYiImageRequest imageRequest = createRequest(imagePrompt, requestImageOptions);

		var observationContext = ImageModelObservationContext.builder()
			.imagePrompt(imagePrompt)
			.provider(TongYiAiApiConstants.PROVIDER_NAME)
			.requestOptions(requestImageOptions)
			.build();

		return ImageModelObservationDocumentation.IMAGE_MODEL_OPERATION
			.observation(this.observationConvention, DEFAULT_OBSERVATION_CONVENTION, () -> observationContext,
					this.observationRegistry)
			.observe(() -> {
				ResponseEntity<TongYiAiImageApi.TongYiImageResponse> imageResponseEntity = this.retryTemplate
					.execute(ctx -> this.tongYiAiImageApi.createImage(imageRequest));

				ImageResponse imageResponse = convertResponse(imageResponseEntity, imageRequest);

				observationContext.setResponse(imageResponse);

				return imageResponse;
			});
	}

	private TongYiAiImageApi.TongYiImageRequest createRequest(ImagePrompt imagePrompt,
			TongYiImageOptions requestImageOptions) {
		String instructions = imagePrompt.getInstructions().get(0).getText();

		TongYiAiImageApi.TongYiImageRequest imageRequest = new TongYiAiImageApi.TongYiImageRequest(new TongYiAiImageApi.Input(instructions),
				TongYiAiImageApi.DEFAULT_IMAGE_MODEL, new TongYiAiImageApi.Parameters(1));

		return ModelOptionsUtils.merge(requestImageOptions, imageRequest, TongYiAiImageApi.TongYiImageRequest.class);
	}

	private ImageResponse convertResponse(ResponseEntity<TongYiAiImageApi.TongYiImageResponse> imageResponseEntity,
										  TongYiAiImageApi.TongYiImageRequest openAiImageRequest) {
		TongYiAiImageApi.TongYiImageResponse imageApiResponse = imageResponseEntity.getBody();
		if (imageApiResponse == null) {
			logger.warn("No image response returned for request: {}", openAiImageRequest);
			return new ImageResponse(List.of());
		}

		// TODO
		//  还需要2次调用接口 GET https://dashscope.aliyuncs.com/api/v1/tasks/{task_id}
		// output.task_status 返回SUCCEEDED 之后获取
		/**
		 * "output":{
		 *         "task_id":"86ecf553-d340-4e21-af6e-a0c6a421c010",
		 *         "task_status":"SUCCEEDED",
		 *         "results":[
		 *             {
		 *                 "url":"https://dashscope-result-bj.oss-cn-beijing.aliyuncs.com/123/a1.png
		 *             },
		 *             {
		 *                 "url":"https://dashscope-result-bj.oss-cn-beijing.aliyuncs.com/123/b2.png
		 *             }
		 *         ],
		 *         "task_metrics":{
		 *             "TOTAL":2,
		 *             "SUCCEEDED":2,
		 *             "FAILED":0
		 *         }
		 *     },
		 *     "usage":{
		 *         "image_count":2
		 *     }
		 */

		return new ImageResponse(List.of());

	}

	/**
	 * Merge runtime and default {@link ImageOptions} to compute the final options to use
	 * in the request.
	 */
	private TongYiImageOptions mergeOptions(@Nullable ImageOptions runtimeOptions, TongYiImageOptions defaultOptions) {
		var runtimeOptionsForProvider = ModelOptionsUtils.copyToTarget(runtimeOptions, ImageOptions.class,
				TongYiImageOptions.class);

		if (runtimeOptionsForProvider == null) {
			return defaultOptions;
		}

		return TongYiImageOptions.builder()
			// Handle portable image options
			.withModel(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getModel(), defaultOptions.getModel()))
			.withN(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getN(), defaultOptions.getN()))
			.withResponseFormat(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getResponseFormat(),
					defaultOptions.getResponseFormat()))
			.withWidth(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getWidth(), defaultOptions.getWidth()))
			.withHeight(
					ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getHeight(), defaultOptions.getHeight()))
			.withStyle(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getStyle(), defaultOptions.getStyle()))
			// Handle TongYiAI specific image options
			.withQuality(
					ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getQuality(), defaultOptions.getQuality()))
			.withUser(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getUser(), defaultOptions.getUser()))
			.build();
	}

	/**
	 * Use the provided convention for reporting observation data
	 * @param observationConvention The provided convention
	 */
	public void setObservationConvention(ImageModelObservationConvention observationConvention) {
		Assert.notNull(observationConvention, "observationConvention cannot be null");
		this.observationConvention = observationConvention;
	}

}
