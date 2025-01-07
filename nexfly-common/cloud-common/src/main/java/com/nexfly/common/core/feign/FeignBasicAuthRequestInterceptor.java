package com.nexfly.common.core.feign;

import com.alibaba.cloud.commons.lang.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@ConditionalOnClass({RequestInterceptor.class})
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {

    @Autowired
    private FeignInsideAuthConfig feignInsideAuthConfig;

    @Override
    public void apply(RequestTemplate template) {
        // feign的内部请求，往请求头放入key 和 secret进行校验
        template.header(feignInsideAuthConfig.getKey(), feignInsideAuthConfig.getSecret());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader("Authorization");

        if (StringUtils.isNotBlank(authorization)) {
            template.header("Authorization", authorization);
        }
    }

}
