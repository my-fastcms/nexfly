package com.nexfly.ai.tongyi;

import com.nexfly.ai.tongyi.api.TongYiAiAudioApi;
import com.nexfly.ai.tongyi.audio.speech.*;
import com.nexfly.ai.tongyi.metadata.audio.TongYiAudioSpeechResponseMetadata;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.openai.metadata.support.OpenAiResponseHeaderExtractor;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

public class TongYiAudioSpeechModel implements SpeechModel, StreamingSpeechModel {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * The default options used for the audio completion requests.
	 */
	private final TongYiAudioSpeechOptions defaultOptions;

	/**
	 * The speed of the default voice synthesis.
	 * @see TongYiAudioSpeechOptions
	 */
	private static final Float SPEED = 1.0f;

	/**
	 * The retry template used to retry the OpenAI Audio API calls.
	 */
	private final RetryTemplate retryTemplate;

	/**
	 * Low-level access to the OpenAI Audio API.
	 */
	private final TongYiAiAudioApi audioApi;

	/**
	 * Initializes a new instance of the OpenAiAudioSpeechModel class with the provided
	 * OpenAiAudioApi. It uses the model tts-1, response format mp3, voice alloy, and the
	 * default speed of 1.0.
	 * @param audioApi The OpenAiAudioApi to use for speech synthesis.
	 */
	public TongYiAudioSpeechModel(TongYiAiAudioApi audioApi) {
		this(audioApi,
				TongYiAudioSpeechOptions.builder()
					.withModel(TongYiAiAudioApi.TtsModel.TTS_1.getValue())
					.withResponseFormat(TongYiAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
					.withVoice(TongYiAiAudioApi.SpeechRequest.Voice.ALLOY)
					.withSpeed(SPEED)
					.build());
	}

	/**
	 * Initializes a new instance of the OpenAiAudioSpeechModel class with the provided
	 * OpenAiAudioApi and options.
	 * @param audioApi The OpenAiAudioApi to use for speech synthesis.
	 * @param options The OpenAiAudioSpeechOptions containing the speech synthesis
	 * options.
	 */
	public TongYiAudioSpeechModel(TongYiAiAudioApi audioApi, TongYiAudioSpeechOptions options) {
		this(audioApi, options, RetryUtils.DEFAULT_RETRY_TEMPLATE);
	}

	/**
	 * Initializes a new instance of the OpenAiAudioSpeechModel class with the provided
	 * OpenAiAudioApi and options.
	 * @param audioApi The OpenAiAudioApi to use for speech synthesis.
	 * @param options The OpenAiAudioSpeechOptions containing the speech synthesis
	 * options.
	 * @param retryTemplate The retry template.
	 */
	public TongYiAudioSpeechModel(TongYiAiAudioApi audioApi, TongYiAudioSpeechOptions options,
								  RetryTemplate retryTemplate) {
		Assert.notNull(audioApi, "OpenAiAudioApi must not be null");
		Assert.notNull(options, "OpenAiSpeechOptions must not be null");
		Assert.notNull(options, "RetryTemplate must not be null");
		this.audioApi = audioApi;
		this.defaultOptions = options;
		this.retryTemplate = retryTemplate;
	}

	@Override
	public byte[] call(String text) {
		SpeechPrompt speechRequest = new SpeechPrompt(text);
		return call(speechRequest).getResult().getOutput();
	}

	@Override
	public SpeechResponse call(SpeechPrompt speechPrompt) {

		TongYiAiAudioApi.SpeechRequest speechRequest = createRequest(speechPrompt);

		ResponseEntity<byte[]> speechEntity = this.retryTemplate
			.execute(ctx -> this.audioApi.createSpeech(speechRequest));

		var speech = speechEntity.getBody();

		if (speech == null) {
			logger.warn("No speech response returned for speechRequest: {}", speechRequest);
			return new SpeechResponse(new Speech(new byte[0]));
		}

		RateLimit rateLimits = OpenAiResponseHeaderExtractor.extractAiResponseHeaders(speechEntity);

		return new SpeechResponse(new Speech(speech), new TongYiAudioSpeechResponseMetadata(rateLimits));
	}

	/**
	 * Streams the audio response for the given speech prompt.
	 * @param speechPrompt The speech prompt containing the text and options for speech
	 * synthesis.
	 * @return A Flux of SpeechResponse objects containing the streamed audio and
	 * metadata.
	 */
	@Override
	public Flux<SpeechResponse> stream(SpeechPrompt speechPrompt) {

		TongYiAiAudioApi.SpeechRequest speechRequest = createRequest(speechPrompt);

		Flux<ResponseEntity<byte[]>> speechEntity = this.retryTemplate
			.execute(ctx -> this.audioApi.stream(speechRequest));

		return speechEntity.map(entity -> new SpeechResponse(new Speech(entity.getBody()),
				new TongYiAudioSpeechResponseMetadata(OpenAiResponseHeaderExtractor.extractAiResponseHeaders(entity))));
	}

	private TongYiAiAudioApi.SpeechRequest createRequest(SpeechPrompt request) {
		TongYiAudioSpeechOptions options = this.defaultOptions;

		if (request.getOptions() != null) {
			if (request.getOptions() instanceof TongYiAudioSpeechOptions runtimeOptions) {
				options = this.merge(runtimeOptions, options);
			}
			else {
				throw new IllegalArgumentException("Prompt options are not of type SpeechOptions: "
						+ request.getOptions().getClass().getSimpleName());
			}
		}

		String input = StringUtils.isNotBlank(options.getInput()) ? options.getInput()
				: request.getInstructions().getText();

		TongYiAiAudioApi.SpeechRequest.Builder requestBuilder = TongYiAiAudioApi.SpeechRequest.builder()
			.withModel(options.getModel())
			.withInput(input)
			.withVoice(options.getVoice())
			.withResponseFormat(options.getResponseFormat())
			.withSpeed(options.getSpeed());

		return requestBuilder.build();
	}

	private TongYiAudioSpeechOptions merge(TongYiAudioSpeechOptions source, TongYiAudioSpeechOptions target) {
		TongYiAudioSpeechOptions.Builder mergedBuilder = TongYiAudioSpeechOptions.builder();

		mergedBuilder.withModel(source.getModel() != null ? source.getModel() : target.getModel());
		mergedBuilder.withInput(source.getInput() != null ? source.getInput() : target.getInput());
		mergedBuilder.withVoice(source.getVoice() != null ? source.getVoice() : target.getVoice());
		mergedBuilder.withResponseFormat(
				source.getResponseFormat() != null ? source.getResponseFormat() : target.getResponseFormat());
		mergedBuilder.withSpeed(source.getSpeed() != null ? source.getSpeed() : target.getSpeed());

		return mergedBuilder.build();
	}

}
