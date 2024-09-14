package com.nexfly.ai.common.provider;

/**
 * @Author wangjun
 * @Date 2024/8/24
 **/
public class EmbedProperties extends ModelProperties {

    Integer maxChunks;

    Integer maxCharactersPerChunk;

    public Integer getMaxChunks() {
        return maxChunks;
    }

    public void setMaxChunks(Integer maxChunks) {
        this.maxChunks = maxChunks;
    }

    public Integer getMaxCharactersPerChunk() {
        return maxCharactersPerChunk;
    }

    public void setMaxCharactersPerChunk(Integer maxCharactersPerChunk) {
        this.maxCharactersPerChunk = maxCharactersPerChunk;
    }
}
