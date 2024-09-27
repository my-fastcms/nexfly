package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.stereotype.Service;

/**
 * 智谱
 */
@Service("zhipu-chat")
public class ZhipuChatModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return new ZhiPuAiChatModel(new ZhiPuAiApi(getApiKey()), ZhiPuAiChatOptions.builder().withModel(createModel.getModelName()).build());
    }

}
