package com.nexfly.auth.handler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author wangjun
 **/
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2")
public class OAuth2Properties {

    private Map<String, String> successUrls;

    public Map<String, String> getSuccessUrls() {
        return successUrls;
    }

    public void setSuccessUrls(Map<String, String> successUrls) {
        this.successUrls = successUrls;
    }

}
