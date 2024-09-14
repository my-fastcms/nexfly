package com.nexfly.ai.common.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * text 2 audio or audio 2 text
 */
@Service("tongyi-att")
public class TongYiAudioTranscriptionModelFactory extends AbstractModelFactory {

    @Override
    Object doCreate(CreateModel createModel, JSONObject jsonObject) {
        return null;
    }

}
