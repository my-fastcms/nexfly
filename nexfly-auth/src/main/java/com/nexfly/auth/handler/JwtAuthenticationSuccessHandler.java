package com.nexfly.auth.handler;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.nexfly.common.auth.model.UserInfo;
import com.nexfly.common.auth.token.AccessToken;
import com.nexfly.common.auth.token.TokenManager;
import com.nexfly.common.core.rest.RestResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    HttpMessageConverter<Object> httpMessageConverter = new FastJsonHttpMessageConverter();

    @Autowired
    private TokenManager tokenManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        AccessToken token = tokenManager.createToken((UserInfo) authentication.getPrincipal());
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpMessageConverter.write(RestResultUtils.success(token), MediaType.APPLICATION_JSON, httpResponse);
    }

}
