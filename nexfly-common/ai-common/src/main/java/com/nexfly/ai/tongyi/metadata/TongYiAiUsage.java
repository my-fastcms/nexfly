package com.nexfly.ai.tongyi.metadata;

import com.nexfly.ai.tongyi.api.TongYiAiApi;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.util.Assert;

public class TongYiAiUsage implements Usage {

	public static TongYiAiUsage from(TongYiAiApi.Usage usage) {
		return new TongYiAiUsage(usage);
	}

	private final TongYiAiApi.Usage usage;

	protected TongYiAiUsage(TongYiAiApi.Usage usage) {
		Assert.notNull(usage, "TongYiAi Usage must not be null");
		this.usage = usage;
	}

	protected TongYiAiApi.Usage getUsage() {
		return this.usage;
	}

	@Override
	public Long getPromptTokens() {
		Integer promptTokens = getUsage().promptTokens();
		return promptTokens != null ? promptTokens.longValue() : 0;
	}

	@Override
	public Long getGenerationTokens() {
		Integer generationTokens = getUsage().completionTokens();
		return generationTokens != null ? generationTokens.longValue() : 0;
	}

	@Override
	public Long getTotalTokens() {
		Integer totalTokens = getUsage().totalTokens();
		if (totalTokens != null) {
			return totalTokens.longValue();
		}
		else {
			return getPromptTokens() + getGenerationTokens();
		}
	}

	@Override
	public String toString() {
		return getUsage().toString();
	}

}
