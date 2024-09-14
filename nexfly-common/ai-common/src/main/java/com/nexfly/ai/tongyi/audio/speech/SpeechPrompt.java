package com.nexfly.ai.tongyi.audio.speech;

import com.nexfly.ai.tongyi.TongYiAudioSpeechOptions;
import org.springframework.ai.model.ModelOptions;
import org.springframework.ai.model.ModelRequest;
import org.springframework.ai.openai.audio.speech.SpeechMessage;

import java.util.Objects;

public class SpeechPrompt implements ModelRequest<SpeechMessage> {

	private TongYiAudioSpeechOptions speechOptions;

	private final SpeechMessage message;

	public SpeechPrompt(String instructions) {
		this(new SpeechMessage(instructions), TongYiAudioSpeechOptions.builder().build());
	}

	public SpeechPrompt(String instructions, TongYiAudioSpeechOptions speechOptions) {
		this(new SpeechMessage(instructions), speechOptions);
	}

	public SpeechPrompt(SpeechMessage speechMessage) {
		this(speechMessage, TongYiAudioSpeechOptions.builder().build());
	}

	public SpeechPrompt(SpeechMessage speechMessage, TongYiAudioSpeechOptions speechOptions) {
		this.message = speechMessage;
		this.speechOptions = speechOptions;
	}

	@Override
	public SpeechMessage getInstructions() {
		return this.message;
	}

	@Override
	public ModelOptions getOptions() {
		return speechOptions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof SpeechPrompt that))
			return false;
		return Objects.equals(speechOptions, that.speechOptions) && Objects.equals(message, that.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(speechOptions, message);
	}

}
