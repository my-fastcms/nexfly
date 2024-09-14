package com.nexfly.ai.tongyi.audio.speech;

import com.nexfly.ai.tongyi.metadata.audio.TongYiAudioSpeechMetadata;
import org.springframework.ai.model.ModelResult;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class Speech implements ModelResult<byte[]> {

	private final byte[] audio;

	private TongYiAudioSpeechMetadata speechMetadata;

	public Speech(byte[] audio) {
		this.audio = audio;
	}

	@Override
	public byte[] getOutput() {
		return this.audio;
	}

	@Override
	public TongYiAudioSpeechMetadata getMetadata() {
		return speechMetadata != null ? speechMetadata : TongYiAudioSpeechMetadata.NULL;
	}

	public Speech withSpeechMetadata(@Nullable TongYiAudioSpeechMetadata speechMetadata) {
		this.speechMetadata = speechMetadata;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Speech that))
			return false;
		return Arrays.equals(audio, that.audio) && Objects.equals(speechMetadata, that.speechMetadata);
	}

	@Override
	public int hashCode() {
		return Objects.hash(Arrays.hashCode(audio), speechMetadata);
	}

	@Override
	public String toString() {
		return "Speech{" + "text=" + audio + ", speechMetadata=" + speechMetadata + '}';
	}

}