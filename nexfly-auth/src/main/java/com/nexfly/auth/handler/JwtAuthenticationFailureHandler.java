package com.nexfly.auth.handler;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.nexfly.common.core.rest.RestResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    HttpMessageConverter<Object> httpMessageConverter = new FastJsonHttpMessageConverter();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpMessageConverter.write(RestResultUtils.failed(exception.getMessage()), MediaType.APPLICATION_JSON, httpResponse);
    }
}
