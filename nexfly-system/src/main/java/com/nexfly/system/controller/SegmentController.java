package com.nexfly.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.model.DocumentSegment;
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
    public RestResult<PageInfo<DocumentSegment>> list(@RequestBody SegmentService.SegmentListRequest segmentListRequest) {
        PageInfo<DocumentSegment> documentPage = PageHelper.startPage(segmentListRequest.page(), segmentListRequest.pageSize()).doSelectPageInfo(() -> segmentService.list(segmentListRequest));
        return RestResultUtils.success(documentPage);
    }

    @PostMapping("retrieval/test")
    public RestResult<SegmentService.RetrievalTestResponse> test(@RequestBody SegmentService.RetrievalTestRequest retrievalTestRequest) {
        return RestResultUtils.success(segmentService.retrievalTest(retrievalTestRequest));
    }

    @GetMapping("get")
    public RestResult<Object> getSegment(@RequestParam("segmentId") Long segmentId) {
        return RestResultUtils.success(segmentService.getById(segmentId));
    }

    @PostMapping("create")
    public RestResult<Object> create(@RequestBody SegmentService.UpdateSegmentRequest updateSegmentRequest) {
        segmentService.update(updateSegmentRequest);
        return RestResultUtils.success();
    }

    @PostMapping("delete")
    public RestResult<Object> delete(@RequestBody SegmentService.DeleteSegmentRequest deleteSegmentRequest) {
        segmentService.delete(deleteSegmentRequest);
        return RestResultUtils.success();
    }

    @PostMapping("switch")
    public RestResult<Object> switchEnable(@RequestBody SegmentService.SwitchEnableRequest switchEnableRequest) {
        segmentService.switchEnable(switchEnableRequest);
        return RestResultUtils.success();
    }

    @GetMapping("knowledge/graph")
    public RestResult<Long> getKnowledgeGraph(@RequestParam("documentId") Long documentId) {
        return RestResultUtils.success(documentId);
    }

}
