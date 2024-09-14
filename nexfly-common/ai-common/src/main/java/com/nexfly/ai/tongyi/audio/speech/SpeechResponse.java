package com.nexfly.ai.tongyi.audio.speech;

import com.nexfly.ai.tongyi.metadata.audio.TongYiAudioSpeechResponseMetadata;
import org.springframework.ai.model.ModelResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SpeechResponse implements ModelResponse<Speech> {

	private final Speech speech;

	private final TongYiAudioSpeechResponseMetadata speechResponseMetadata;

	/**
	 * Creates a new instance of SpeechResponse with the given speech result.
	 * @param speech the speech result to be set in the SpeechResponse
	 * @see org.springframework.ai.openai.audio.speech.Speech
	 */
	public SpeechResponse(Speech speech) {
		this(speech, TongYiAudioSpeechResponseMetadata.NULL);
	}

	/**
	 * Creates a new instance of SpeechResponse with the given speech result and speech
	 * response metadata.
	 * @param speech the speech result to be set in the SpeechResponse
	 * @param speechResponseMetadata the speech response metadata to be set in the
	 * SpeechResponse
	 * @see org.springframework.ai.openai.audio.speech.Speech
	 * @see TongYiAudioSpeechResponseMetadata
	 */
	public SpeechResponse(Speech speech, TongYiAudioSpeechResponseMetadata speechResponseMetadata) {
		this.speech = speech;
		this.speechResponseMetadata = speechResponseMetadata;
	}

	@Override
	public Speech getResult() {
		return speech;
	}

	@Override
	public List<Speech> getResults() {
		return Collections.singletonList(speech);
	}

	@Override
	public TongYiAudioSpeechResponseMetadata getMetadata() {
		return speechResponseMetadata;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof SpeechResponse that))
			return false;
		return Objects.equals(speech, that.speech)
				&& Objects.equals(speechResponseMetadata, that.speechResponseMetadata);
	}

	@Override
	public int hashCode() {
		return Objects.hash(speech, speechResponseMetadata);
	}

}