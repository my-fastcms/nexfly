package com.nexfly.system.controller;

import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.model.Attachment;
import com.nexfly.system.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wangjun
 **/
@RestController
@RequestMapping(NexflyConstants.API_PREFIX_MAPPING + "/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("upload")
    @ExceptionHandler(value = MultipartException.class)
    public RestResult<List<Long>> upload(@RequestParam("file") MultipartFile[] files) {
        if(files == null) {
            return RestResultUtils.failed("请选择上传文件");
        }
        List<Long> attachmentList = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Attachment attachment = attachmentService.upload(
                        Objects.requireNonNull(file.getOriginalFilename()),
                        file.getSize(),
                        Objects.requireNonNull(file.getContentType()),
                        file.getInputStream());
                attachmentList.add(attachment.getAttachmentId());
            } catch (Exception e) {
            }
        }

        return RestResultUtils.success(attachmentList);
    }

    @PostMapping("list_by_ids")
    public RestResult<List<Attachment>> listByIds(@RequestBody AttachmentService.ListByIdsRequest listByIdsRequest) {
        return RestResultUtils.success(attachmentService.getAttachmentListByIds(listByIdsRequest));
    }

    @PostMapping("delete")
    public RestResult<List<Attachment>> delete(@RequestBody AttachmentService.DeleteRequest deleteRequest) {
        attachmentService.delete(deleteRequest);
        return RestResultUtils.success();
    }

}
