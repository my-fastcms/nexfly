package com.nexfly.common.auth.filter;

import com.nexfly.common.auth.token.TokenManager;
import com.nexfly.common.core.constants.NexflyConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        if (!req.getRequestURI().contains(NexflyConstants.API_PREFIX_MAPPING)) {
            filterChain.doFilter(request, response);
        } else {
            final String jwt = tokenManager.resolveToken((HttpServletRequest) request);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            if (StringUtils.isBlank(jwt)) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "not auth");
                return;
            }
            try {
                tokenManager.validateToken(jwt);
                Authentication authentication = tokenManager.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException | SignatureException e) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

}
