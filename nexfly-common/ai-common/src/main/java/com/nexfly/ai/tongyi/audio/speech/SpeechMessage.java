package com.nexfly.ai.tongyi.audio.speech;

import java.util.Objects;

public class SpeechMessage {

	private String text;

	/**
	 * Constructs a new {@link SpeechMessage} object with the given text.
	 * @param text the text to be converted to speech
	 */
	public SpeechMessage(String text) {
		this.text = text;
	}

	/**
	 * Returns the text of this speech message.
	 * @return the text of this speech message
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text of this speech message.
	 * @param text the new text for this speech message
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof SpeechMessage that))
			return false;
		return Objects.equals(text, that.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(text);
	}

}