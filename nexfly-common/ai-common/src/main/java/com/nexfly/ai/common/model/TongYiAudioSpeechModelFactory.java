package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import com.nexfly.ai.tongyi.TongYiAudioSpeechModel;
import com.nexfly.ai.tongyi.api.TongYiAiAudioApi;
import org.springframework.stereotype.Service;

@Service("tongyi-qianwen-tts")
public class TongYiAudioSpeechModelFactory extends AbstractModelFactory {
    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return new TongYiAudioSpeechModel(new TongYiAiAudioApi(getApiKey()));
    }

}
