package com.nexfly.system.controller;

import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.model.App;
import com.nexfly.system.model.AppConversation;
import com.nexfly.system.service.AppService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping(NexflyConstants.API_PREFIX_MAPPING + "/app")
public class AppController {

    @Autowired
    private AppService appService;

    @PostMapping(value = "chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AppService.ChatResponse> chat(@RequestBody AppService.NexflyMessage message) throws Exception {
        return appService.chat(message);
    }

    @GetMapping("list")
    public RestResult<List<App>> list() {
        return RestResultUtils.success(appService.list(AuthUtils.getUserId()));
    }

    @GetMapping("get")
    public RestResult<App> get(@RequestParam("appId") Long appId) {
        return RestResultUtils.success(appService.findById(appId));
    }

    @PostMapping("conversation/set")
    public RestResult<AppConversation> setConversation(@RequestBody AppConversation appConversation) {
        appService.saveAppConversation(appConversation);
        return RestResultUtils.success(appConversation);
    }

    @GetMapping("conversation/get")
    public RestResult<AppService.Conversation> getConversation(@Param("conversationId") Long conversationId) {
        return RestResultUtils.success(appService.getAppConversation(conversationId));
    }

    @GetMapping("conversation/list")
    public RestResult<List<AppConversation>> getConversationList(@RequestParam("appId") Long appId) {
        return RestResultUtils.success(appService.getAppConversationList(appId));
    }

    @PostMapping("conversation/delete")
    public RestResult<List<AppConversation>> deleteConversation(@RequestParam("appId") Long appId,
                                                                @RequestParam("conversationId") Long conversationId) {
        appService.deleteAppConversation(appId, conversationId);
        return RestResultUtils.success();
    }

}
