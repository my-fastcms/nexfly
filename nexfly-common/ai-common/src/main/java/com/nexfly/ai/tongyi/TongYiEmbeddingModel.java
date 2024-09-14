package com.nexfly.ai.tongyi;

import com.nexfly.ai.tongyi.api.TongYiAiApi;
import com.nexfly.ai.tongyi.api.TongYiAiApiConstants;
import com.nexfly.ai.tongyi.metadata.TongYiAiUsage;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.*;
import org.springframework.ai.embedding.observation.DefaultEmbeddingModelObservationConvention;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationContext;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationConvention;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationDocumentation;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.lang.Nullable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;

import java.util.List;

/**
 * TongYi AI Embedding Model implementation.
 *
 * @author wangjun
 */
public class TongYiEmbeddingModel extends AbstractEmbeddingModel {

	private static final Logger logger = LoggerFactory.getLogger(TongYiEmbeddingModel.class);

	private static final EmbeddingModelObservationConvention DEFAULT_OBSERVATION_CONVENTION = new DefaultEmbeddingModelObservationConvention();

	private final TongYiEmbeddingOptions defaultOptions;

	private final RetryTemplate retryTemplate;

	private final TongYiAiApi tongYiAiApi;

	private final MetadataMode metadataMode;

	/**
	 * Observation registry used for instrumentation.
	 */
	private final ObservationRegistry observationRegistry;

	/**
	 * Conventions to use for generating observations.
	 */
	private EmbeddingModelObservationConvention observationConvention = DEFAULT_OBSERVATION_CONVENTION;

	/**
	 * Constructor for the OpenAiEmbeddingModel class.
	 * @param tongYiAiApi The OpenAiApi instance to use for making API requests.
	 */
	public TongYiEmbeddingModel(TongYiAiApi tongYiAiApi) {
		this(tongYiAiApi, MetadataMode.EMBED);
	}

	/**
	 * Initializes a new instance of the OpenAiEmbeddingModel class.
	 * @param tongYiAiApi The OpenAiApi instance to use for making API requests.
	 * @param metadataMode The mode for generating metadata.
	 */
	public TongYiEmbeddingModel(TongYiAiApi tongYiAiApi, MetadataMode metadataMode) {
		this(tongYiAiApi, metadataMode,
				TongYiEmbeddingOptions.builder().withModel(TongYiAiApi.DEFAULT_EMBEDDING_MODEL).build());
	}

	/**
	 * Initializes a new instance of the OpenAiEmbeddingModel class.
	 * @param tongYiAiApi The OpenAiApi instance to use for making API requests.
	 * @param metadataMode The mode for generating metadata.
	 * @param openAiEmbeddingOptions The options for OpenAi embedding.
	 */
	public TongYiEmbeddingModel(TongYiAiApi tongYiAiApi, MetadataMode metadataMode,
								TongYiEmbeddingOptions openAiEmbeddingOptions) {
		this(tongYiAiApi, metadataMode, openAiEmbeddingOptions, RetryUtils.DEFAULT_RETRY_TEMPLATE);
	}

	/**
	 * Initializes a new instance of the OpenAiEmbeddingModel class.
	 * @param tongYiAiApi - The OpenAiApi instance to use for making API requests.
	 * @param metadataMode - The mode for generating metadata.
	 * @param options - The options for OpenAI embedding.
	 * @param retryTemplate - The RetryTemplate for retrying failed API requests.
	 */
	public TongYiEmbeddingModel(TongYiAiApi tongYiAiApi, MetadataMode metadataMode, TongYiEmbeddingOptions options,
								RetryTemplate retryTemplate) {
		this(tongYiAiApi, metadataMode, options, retryTemplate, ObservationRegistry.NOOP);
	}

	/**
	 * Initializes a new instance of the OpenAiEmbeddingModel class.
	 * @param tongYiAiApi - The OpenAiApi instance to use for making API requests.
	 * @param metadataMode - The mode for generating metadata.
	 * @param options - The options for OpenAI embedding.
	 * @param retryTemplate - The RetryTemplate for retrying failed API requests.
	 * @param observationRegistry - The ObservationRegistry used for instrumentation.
	 */
	public TongYiEmbeddingModel(TongYiAiApi tongYiAiApi, MetadataMode metadataMode, TongYiEmbeddingOptions options,
								RetryTemplate retryTemplate, ObservationRegistry observationRegistry) {
		Assert.notNull(tongYiAiApi, "tongYiAiApi must not be null");
		Assert.notNull(metadataMode, "metadataMode must not be null");
		Assert.notNull(options, "options must not be null");
		Assert.notNull(retryTemplate, "retryTemplate must not be null");
		Assert.notNull(observationRegistry, "observationRegistry must not be null");

		this.tongYiAiApi = tongYiAiApi;
		this.metadataMode = metadataMode;
		this.defaultOptions = options;
		this.retryTemplate = retryTemplate;
		this.observationRegistry = observationRegistry;
	}

	@Override
	public float[] embed(Document document) {
		Assert.notNull(document, "Document must not be null");
		return this.embed(document.getFormattedContent(this.metadataMode));
	}

	@Override
	public EmbeddingResponse call(EmbeddingRequest request) {
		TongYiEmbeddingOptions requestOptions = mergeOptions(request.getOptions(), this.defaultOptions);
		TongYiAiApi.EmbeddingRequest<List<String>> apiRequest = createRequest(request, requestOptions);

		var observationContext = EmbeddingModelObservationContext.builder()
			.embeddingRequest(request)
			.provider(TongYiAiApiConstants.PROVIDER_NAME)
			.requestOptions(requestOptions)
			.build();

		return EmbeddingModelObservationDocumentation.EMBEDDING_MODEL_OPERATION
			.observation(this.observationConvention, DEFAULT_OBSERVATION_CONVENTION, () -> observationContext,
					this.observationRegistry)
			.observe(() -> {
				TongYiAiApi.EmbeddingList<TongYiAiApi.Embedding> apiEmbeddingResponse = this.retryTemplate
					.execute(ctx -> this.tongYiAiApi.embeddings(apiRequest).getBody());

				if (apiEmbeddingResponse == null) {
					logger.warn("No embeddings returned for request: {}", request);
					return new EmbeddingResponse(List.of());
				}

				var metadata = new EmbeddingResponseMetadata(apiEmbeddingResponse.model(),
						TongYiAiUsage.from(apiEmbeddingResponse.usage()));

				List<Embedding> embeddings = apiEmbeddingResponse.output().embeddings()
					.stream()
					.map(e -> new Embedding(e.embedding(), e.index()))
					.toList();

				EmbeddingResponse embeddingResponse = new EmbeddingResponse(embeddings, metadata);

				observationContext.setResponse(embeddingResponse);

				return embeddingResponse;
			});
	}

	private TongYiAiApi.EmbeddingRequest<List<String>> createRequest(EmbeddingRequest request,
			TongYiEmbeddingOptions requestOptions) {
		return new TongYiAiApi.EmbeddingRequest<>(new TongYiAiApi.EmbeddingRequest.Input(request.getInstructions()), requestOptions.getModel());
	}

	/**
	 * Merge runtime and default {@link EmbeddingOptions} to compute the final options to
	 * use in the request.
	 */
	private TongYiEmbeddingOptions mergeOptions(@Nullable EmbeddingOptions runtimeOptions,
												TongYiEmbeddingOptions defaultOptions) {
		var runtimeOptionsForProvider = ModelOptionsUtils.copyToTarget(runtimeOptions, EmbeddingOptions.class,
				TongYiEmbeddingOptions.class);

		if (runtimeOptionsForProvider == null) {
			return defaultOptions;
		}

		return TongYiEmbeddingOptions.builder()
			// Handle portable embedding options
			.withModel(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getModel(), defaultOptions.getModel()))
			.withDimensions(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getDimensions(),
					defaultOptions.getDimensions()))
			// Handle OpenAI specific embedding options
			.withEncodingFormat(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getEncodingFormat(),
					defaultOptions.getEncodingFormat()))
			.withUser(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getUser(), defaultOptions.getUser()))
			.build();
	}

	/**
	 * Use the provided convention for reporting observation data
	 * @param observationConvention The provided convention
	 */
	public void setObservationConvention(EmbeddingModelObservationConvention observationConvention) {
		Assert.notNull(observationConvention, "observationConvention cannot be null");
		this.observationConvention = observationConvention;
	}

}
