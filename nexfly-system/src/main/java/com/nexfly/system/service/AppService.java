package com.nexfly.system.service;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
public interface AppService {

    Flux<String> chat(Long appId, String message) throws Exception;

}
