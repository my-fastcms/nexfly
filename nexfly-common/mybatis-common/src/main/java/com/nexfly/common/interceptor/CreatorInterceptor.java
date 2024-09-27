package com.nexfly.common.interceptor;

import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.model.BaseModel;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class })
})
public class CreatorInterceptor implements Interceptor {

    final String CREATEBY_METHOD = "setCreateBy";
    final String UPDATEBY_METHOD = "setUpdateBy";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (SqlCommandType.DELETE.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            return invocation.proceed();
        }

        if (parameter instanceof Map<?,?>) {
            parameter = ((MapperMethod.ParamMap<?>) parameter).get(((MapperMethod.ParamMap<?>) parameter).keySet().stream().toList().get(0));
        }
        try {
            if (AuthUtils.getUser() != null && parameter instanceof BaseModel) {
                Method method = parameter.getClass().getSuperclass().getMethod(CREATEBY_METHOD, Long.class);
                method.invoke(parameter, AuthUtils.getUserId());
                method = parameter.getClass().getSuperclass().getMethod(UPDATEBY_METHOD, Long.class);
                method.invoke(parameter, AuthUtils.getUserId());
            }
            return invocation.proceed();
        } catch (Exception e) {
            return invocation.proceed();
        }
    }

}
