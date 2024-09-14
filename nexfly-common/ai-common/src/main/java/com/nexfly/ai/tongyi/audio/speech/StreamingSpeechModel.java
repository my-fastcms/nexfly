package com.nexfly.ai.tongyi.audio.speech;

import org.springframework.ai.model.StreamingModel;
import reactor.core.publisher.Flux;

@FunctionalInterface
public interface StreamingSpeechModel extends StreamingModel<SpeechPrompt, SpeechResponse> {

	/**
	 * Generates a stream of audio bytes from the provided text message.
	 * @param message the text message to be converted to audio
	 * @return a Flux of audio bytes representing the generated speech
	 */
	default Flux<byte[]> stream(String message) {
		SpeechPrompt prompt = new SpeechPrompt(message);
		return stream(prompt).map(SpeechResponse::getResult).map(Speech::getOutput);
	}

	/**
	 * Sends a speech request to the OpenAI TTS API and returns a stream of the resulting
	 * speech responses.
	 * @param prompt the speech prompt containing the input text and other parameters
	 * @return a Flux of speech responses, each containing a portion of the generated
	 * audio
	 */
	@Override
	Flux<SpeechResponse> stream(SpeechPrompt prompt);

}