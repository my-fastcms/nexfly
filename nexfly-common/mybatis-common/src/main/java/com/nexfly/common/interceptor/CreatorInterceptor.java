package com.nexfly.common.interceptor;

import com.nexfly.common.auth.utils.AuthUtils;
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

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (SqlCommandType.DELETE.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        Object parameter = invocation.getArgs()[1];
        if (parameter != null) {
            if (parameter instanceof Map<?,?>) {
                parameter = ((MapperMethod.ParamMap) parameter).get("param1");
            }
            // 假设你有一个静态方法来获取当前登录用户的 ID
            Long userId = AuthUtils.getUserId();
            if (userId != null) {
                // 假设实体类有一个 setCreator() 方法
                Method method = parameter.getClass().getSuperclass().getMethod("setCreateBy", Long.class);
                method.invoke(parameter, userId);
                method = parameter.getClass().getMethod("setUpdateBy", Long.class);
                method.invoke(parameter, userId);
            }
        }
        return invocation.proceed();
    }

}
