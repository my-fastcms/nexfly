package com.nexfly.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.service.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author wangjun
 * @Date 2024/9/29
 **/
@RestController
@RequestMapping(NexflyConstants.API_PREFIX_MAPPING + "/segment")
public class SegmentController {

    @Autowired
    private SegmentService segmentService;

    @PostMapping("list")
    public Object list(@RequestBody SegmentService.SegmentListRequest segmentListRequest) {
        PageInfo<Object> documentPage = PageHelper.startPage(segmentListRequest.page(), segmentListRequest.pageSize()).doSelectPageInfo(() -> segmentService.list(segmentListRequest));
        return RestResultUtils.success(documentPage);
    }

    @GetMapping("knowledge/graph")
    public Object getKnowledgeGraph(@RequestParam("documentId") Long documentId) {
        return RestResultUtils.success(documentId);
    }

}
