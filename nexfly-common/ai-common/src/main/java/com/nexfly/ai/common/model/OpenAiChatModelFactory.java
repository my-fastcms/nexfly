package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

/**
 * open ai
 */
@Service("openai-chat")
public class OpenAiChatModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return new OpenAiChatModel(getOpenAiApi(), OpenAiChatOptions.builder().withModel(createModel.getModelName()).build());
    }

    protected OpenAiApi getOpenAiApi() {
        OpenAiApi openAiApi = new OpenAiApi(getApiKey());
        if (StringUtils.isNotBlank(getApiUrl())) {
            openAiApi = new OpenAiApi(getApiUrl(), getApiKey());
        }
        return openAiApi;
    }

}
