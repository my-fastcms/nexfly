package com.nexfly.auth.handler;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.nexfly.common.core.rest.RestResultUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * @Author wangjun
 * @Date 2024/9/14
 **/
public class NexflyLogoutSuccessHandler implements LogoutSuccessHandler {

    HttpMessageConverter<Object> httpMessageConverter = new FastJsonHttpMessageConverter();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpMessageConverter.write(RestResultUtils.success(), MediaType.APPLICATION_JSON, httpResponse);
    }
}
