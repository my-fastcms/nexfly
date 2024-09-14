package com.nexfly.ai.common.function;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @Author wangjun
 * @Date 2024/9/1
 **/
@Service
public class FunctionManager implements ApplicationListener<ApplicationReadyEvent> {

    List<FunctionInfo> functionList = Collections.synchronizedList(new ArrayList<>());

    public List<FunctionInfo> getFunctionList() {
        return functionList;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Map<String, NexflyFunction> nexflyFunctionMap = event.getApplicationContext().getBeansOfType(NexflyFunction.class);
        nexflyFunctionMap.keySet().forEach(key -> {
            Description annotation = nexflyFunctionMap.get(key).getClass().getAnnotation(Description.class);
            String value = annotation.value();
            if (StringUtils.isNotBlank(value)) {
                functionList.add(new FunctionInfo(key, value, nexflyFunctionMap.get(key)));
            }
        });
    }

    public record FunctionInfo(String name, String description, Function function) {

    }

}
