package com.nexfly.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjun
 **/
public class RedisOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String KEY_PREFIX = "oauth2:authorization_request:";

    private static final String STATE = "state";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String key = buildKey(request);
        return (OAuth2AuthorizationRequest) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        String key = buildKey(authorizationRequest);
        redisTemplate.opsForValue().set(key, authorizationRequest, 5, TimeUnit.MINUTES);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        String key = buildKey(request);
        OAuth2AuthorizationRequest authRequest = (OAuth2AuthorizationRequest) redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return authRequest;
    }

    private String buildKey(OAuth2AuthorizationRequest authorizationRequest) {
        String state = authorizationRequest.getState();
        return KEY_PREFIX + "state:" + state;
    }

    private String buildKey(HttpServletRequest request) {
        String state = request.getParameter(STATE);
        return KEY_PREFIX + "state:" + state;
    }

}
