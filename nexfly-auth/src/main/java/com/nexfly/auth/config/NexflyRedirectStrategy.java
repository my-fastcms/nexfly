package com.nexfly.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author wangjun
 **/
public class NexflyRedirectStrategy extends DefaultRedirectStrategy {

    private final HttpStatus statusCode = HttpStatus.FOUND;

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);

        MultiValueMap<String, String> parameters = getParameters(request);
        for (String key : parameters.keySet()) {
            redirectUrl += "&" + key + "=" + parameters.get(key).get(0);
        }

        redirectUrl = response.encodeRedirectURL(redirectUrl);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Redirecting to %s", redirectUrl));
        }
        if (this.statusCode == HttpStatus.FOUND) {
            response.sendRedirect(redirectUrl);
        }
        else {
            response.setHeader(HttpHeaders.LOCATION, redirectUrl);
            response.setStatus(this.statusCode.value());
            response.getWriter().flush();
        }
    }

    MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values.length > 0) {
                for (String value : values) {
                    parameters.add(key, value);
                }
            }
        });
        return parameters;
    }

}
