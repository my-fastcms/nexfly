package com.nexfly.ai.tongyi.metadata.audio;

import org.springframework.ai.model.ResultMetadata;

public interface TongYiAudioSpeechMetadata extends ResultMetadata {

	TongYiAudioSpeechMetadata NULL = TongYiAudioSpeechMetadata.create();

	/**
	 * Factory method used to construct a new {@link TongYiAudioSpeechMetadata}
	 * @return a new {@link TongYiAudioSpeechMetadata}
	 */
	static TongYiAudioSpeechMetadata create() {
		return new TongYiAudioSpeechMetadata() {
		};
	}

}
