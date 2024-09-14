package com.nexfly.ai.tongyi.metadata.audio;

import com.nexfly.ai.tongyi.api.TongYiAiAudioApi;
import org.springframework.ai.chat.metadata.EmptyRateLimit;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.model.MutableResponseMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class TongYiAudioSpeechResponseMetadata extends MutableResponseMetadata {

	protected static final String AI_METADATA_STRING = "{ @type: %1$s, requestsLimit: %2$s }";

	public static final TongYiAudioSpeechResponseMetadata NULL = new TongYiAudioSpeechResponseMetadata() {
	};

	public static TongYiAudioSpeechResponseMetadata from(TongYiAiAudioApi.StructuredResponse result) {
		Assert.notNull(result, "OpenAI speech must not be null");
		TongYiAudioSpeechResponseMetadata speechResponseMetadata = new TongYiAudioSpeechResponseMetadata();
		return speechResponseMetadata;
	}

	public static TongYiAudioSpeechResponseMetadata from(String result) {
		Assert.notNull(result, "OpenAI speech must not be null");
		TongYiAudioSpeechResponseMetadata speechResponseMetadata = new TongYiAudioSpeechResponseMetadata();
		return speechResponseMetadata;
	}

	@Nullable
	private RateLimit rateLimit;

	public TongYiAudioSpeechResponseMetadata() {
		this(null);
	}

	public TongYiAudioSpeechResponseMetadata(@Nullable RateLimit rateLimit) {
		this.rateLimit = rateLimit;
	}

	@Nullable
	public RateLimit getRateLimit() {
		RateLimit rateLimit = this.rateLimit;
		return rateLimit != null ? rateLimit : new EmptyRateLimit();
	}

	public TongYiAudioSpeechResponseMetadata withRateLimit(RateLimit rateLimit) {
		this.rateLimit = rateLimit;
		return this;
	}

	@Override
	public String toString() {
		return AI_METADATA_STRING.formatted(getClass().getName(), getRateLimit());
	}

}
