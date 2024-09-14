package com.nexfly.ai.tongyi.metadata.audio;

import com.nexfly.ai.tongyi.api.TongYiAiAudioApi;
import com.nexfly.ai.tongyi.metadata.TongYiRateLimit;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponseMetadata;
import org.springframework.ai.chat.metadata.EmptyRateLimit;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class TongYiAudioTranscriptionResponseMetadata extends AudioTranscriptionResponseMetadata {

	protected static final String AI_METADATA_STRING = "{ @type: %1$s, rateLimit: %4$s }";

	public static final TongYiAudioTranscriptionResponseMetadata NULL = new TongYiAudioTranscriptionResponseMetadata() {
	};

	public static TongYiAudioTranscriptionResponseMetadata from(TongYiAiAudioApi.StructuredResponse result) {
		Assert.notNull(result, "OpenAI Transcription must not be null");
		return new TongYiAudioTranscriptionResponseMetadata();
	}

	public static TongYiAudioTranscriptionResponseMetadata from(String result) {
		Assert.notNull(result, "OpenAI Transcription must not be null");
		return new TongYiAudioTranscriptionResponseMetadata();
	}

	@Nullable
	private RateLimit rateLimit;

	protected TongYiAudioTranscriptionResponseMetadata() {
		this(null);
	}

	protected TongYiAudioTranscriptionResponseMetadata(@Nullable TongYiRateLimit rateLimit) {
		this.rateLimit = rateLimit;
	}

	@Nullable
	public RateLimit getRateLimit() {
		RateLimit rateLimit = this.rateLimit;
		return rateLimit != null ? rateLimit : new EmptyRateLimit();
	}

	public TongYiAudioTranscriptionResponseMetadata withRateLimit(RateLimit rateLimit) {
		this.rateLimit = rateLimit;
		return this;
	}

	@Override
	public String toString() {
		return AI_METADATA_STRING.formatted(getClass().getName(), getRateLimit());
	}

}
