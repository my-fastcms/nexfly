package com.nexfly.system.controller;

import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.system.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(NexflyConstants.API_PREFIX_MAPPING + "/chat")
public class ChatController {

    @Autowired
    private AppService appService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(Long appId, String message) throws Exception {
        return appService.chat(appId, message);
    }

}
