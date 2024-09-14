package com.nexfly.ai.common.provider;

/**
 * @Author wangjun
 * @Date 2024/8/24
 **/
public class ModelProperties {

    String mode;

    Integer contextSize;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getContextSize() {
        return contextSize;
    }

    public void setContextSize(Integer contextSize) {
        this.contextSize = contextSize;
    }

}
