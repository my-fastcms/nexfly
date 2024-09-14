package com.nexfly.ai.tongyi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexfly.ai.tongyi.api.TongYiAiAudioApi;
import org.springframework.ai.model.ModelOptions;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TongYiAudioSpeechOptions implements ModelOptions {

	/**
	 * ID of the model to use for generating the audio. One of the available TTS models:
	 * tts-1 or tts-1-hd.
	 */
	@JsonProperty("model")
	private String model;

	/**
	 * The input text to synthesize. Must be at most 4096 tokens long.
	 */
	@JsonProperty("input")
	private String input;

	/**
	 * The voice to use for synthesis. One of the available voices for the chosen model:
	 * 'alloy', 'echo', 'fable', 'onyx', 'nova', and 'shimmer'.
	 */
	@JsonProperty("voice")
	private TongYiAiAudioApi.SpeechRequest.Voice voice;

	/**
	 * The format of the audio output. Supported formats are mp3, opus, aac, and flac.
	 * Defaults to mp3.
	 */
	@JsonProperty("response_format")
	private TongYiAiAudioApi.SpeechRequest.AudioResponseFormat responseFormat;

	/**
	 * The speed of the voice synthesis. The acceptable range is from 0.25 (slowest) to
	 * 4.0 (fastest). Defaults to 1 (normal)
	 */
	@JsonProperty("speed")
	private Float speed;

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private final TongYiAudioSpeechOptions options = new TongYiAudioSpeechOptions();

		public Builder withModel(String model) {
			options.model = model;
			return this;
		}

		public Builder withInput(String input) {
			options.input = input;
			return this;
		}

		public Builder withVoice(TongYiAiAudioApi.SpeechRequest.Voice voice) {
			options.voice = voice;
			return this;
		}

		public Builder withResponseFormat(TongYiAiAudioApi.SpeechRequest.AudioResponseFormat responseFormat) {
			options.responseFormat = responseFormat;
			return this;
		}

		public Builder withSpeed(Float speed) {
			options.speed = speed;
			return this;
		}

		public TongYiAudioSpeechOptions build() {
			return options;
		}

	}

	public String getModel() {
		return model;
	}

	public String getInput() {
		return input;
	}

	public TongYiAiAudioApi.SpeechRequest.Voice getVoice() {
		return voice;
	}

	public TongYiAiAudioApi.SpeechRequest.AudioResponseFormat getResponseFormat() {
		return responseFormat;
	}

	public Float getSpeed() {
		return speed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((voice == null) ? 0 : voice.hashCode());
		result = prime * result + ((responseFormat == null) ? 0 : responseFormat.hashCode());
		result = prime * result + ((speed == null) ? 0 : speed.hashCode());
		return result;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void setVoice(TongYiAiAudioApi.SpeechRequest.Voice voice) {
		this.voice = voice;
	}

	public void setResponseFormat(TongYiAiAudioApi.SpeechRequest.AudioResponseFormat responseFormat) {
		this.responseFormat = responseFormat;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TongYiAudioSpeechOptions other = (TongYiAudioSpeechOptions) obj;
		if (model == null) {
			if (other.model != null)
				return false;
		}
		else if (!model.equals(other.model))
			return false;
		if (input == null) {
			if (other.input != null)
				return false;
		}
		else if (!input.equals(other.input))
			return false;
		if (voice == null) {
			if (other.voice != null)
				return false;
		}
		else if (!voice.equals(other.voice))
			return false;
		if (responseFormat == null) {
			if (other.responseFormat != null)
				return false;
		}
		else if (!responseFormat.equals(other.responseFormat))
			return false;
		if (speed == null) {
			return other.speed == null;
		}
		else
			return speed.equals(other.speed);
	}

	@Override
	public String toString() {
		return "TongYiAudioSpeechOptions{" + "model='" + model + '\'' + ", input='" + input + '\'' + ", voice='" + voice
				+ '\'' + ", responseFormat='" + responseFormat + '\'' + ", speed=" + speed + '}';
	}

}
