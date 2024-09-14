package com.nexfly.ai.tongyi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.embedding.EmbeddingOptions;

/**
 * @author wangjun
 * @since 1.0.0
 */
@JsonInclude(Include.NON_NULL)
public class TongYiEmbeddingOptions implements EmbeddingOptions {

	// @formatter:off
	/**
	 * ID of the model to use.
	 */
	private @JsonProperty("model") String model;
	/**
	 * The format to return the embeddings in. Can be either float or base64.
	 */
	private @JsonProperty("encoding_format") String encodingFormat;
	/**
	 * The number of dimensions the resulting output embeddings should have. Only supported in text-embedding-3 and later models.
	 */
	private @JsonProperty("dimensions") Integer dimensions;
	/**
	 * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
	 */
	private @JsonProperty("user") String user;
	// @formatter:on

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		protected TongYiEmbeddingOptions options;

		public Builder() {
			this.options = new TongYiEmbeddingOptions();
		}

		public Builder withModel(String model) {
			this.options.setModel(model);
			return this;
		}

		public Builder withEncodingFormat(String encodingFormat) {
			this.options.setEncodingFormat(encodingFormat);
			return this;
		}

		public Builder withDimensions(Integer dimensions) {
			this.options.dimensions = dimensions;
			return this;
		}

		public Builder withUser(String user) {
			this.options.setUser(user);
			return this;
		}

		public TongYiEmbeddingOptions build() {
			return this.options;
		}

	}

	@Override
	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getEncodingFormat() {
		return this.encodingFormat;
	}

	public void setEncodingFormat(String encodingFormat) {
		this.encodingFormat = encodingFormat;
	}

	@Override
	public Integer getDimensions() {
		return this.dimensions;
	}

	public void setDimensions(Integer dimensions) {
		this.dimensions = dimensions;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
