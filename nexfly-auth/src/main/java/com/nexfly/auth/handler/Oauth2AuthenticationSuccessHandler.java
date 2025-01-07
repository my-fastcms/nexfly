package com.nexfly.auth.handler;

import com.nexfly.api.system.bean.Oauth2UserInfo;
import com.nexfly.api.system.feign.SystemFeignClient;
import com.nexfly.common.auth.model.UserInfo;
import com.nexfly.common.auth.token.AccessToken;
import com.nexfly.common.auth.token.TokenManager;
import com.nexfly.common.core.rest.RestResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

/**
 * @author wangjun
 **/
public class Oauth2AuthenticationSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SystemFeignClient systemFeignClient;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private OAuth2Properties oauth2Properties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        DefaultOidcUser principal = (DefaultOidcUser) oAuth2AuthenticationToken.getPrincipal();
        String sub = (String) principal.getClaims().get(StandardClaimNames.SUB);
        String name = (String) principal.getClaims().get(StandardClaimNames.NAME);
        String nickname = (String) principal.getClaims().get(StandardClaimNames.NICKNAME);
        String picture = (String) principal.getClaims().get(StandardClaimNames.PICTURE);

        if (StringUtils.isBlank(sub)) {
            throw new ServletException("sub is not allowed null");
        }

        RestResult<SystemFeignClient.AccountInfo> accountInfoRestResult = systemFeignClient.oauth2Login(new Oauth2UserInfo(clientId, sub, name, nickname, picture));
        SystemFeignClient.AccountInfo data = Optional.ofNullable(accountInfoRestResult.getData()).orElseThrow();

        AccessToken token = tokenManager.createToken(new UserInfo(data.accountId(), data.username(), "password"));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(new UserInfo(data.accountId(), data.username(), "password"), "");
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        String baseUrl = oauth2Properties.getSuccessUrls().get(clientId);

        String targetUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("accessToken", token.getToken())
                .build()
                .toUriString();

        setDefaultTargetUrl(targetUrl);

        handle(request, response, authentication);

    }

}
