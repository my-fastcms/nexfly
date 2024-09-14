package com.nexfly.ai.common.provider;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/24
 **/
public class Voice {

    private String name;

    private String mode;

    private List<String> language;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

}
