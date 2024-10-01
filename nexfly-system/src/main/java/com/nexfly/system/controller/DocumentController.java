package com.nexfly.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping(NexflyConstants.API_PREFIX_MAPPING + "/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("upload")
    @ExceptionHandler(value = MultipartException.class)
    public RestResult<Integer> upload(@RequestParam("datasetId") Long datasetId, @RequestParam("file") MultipartFile[] files) throws Exception {
        if(files == null) {
            return RestResultUtils.failed("请选择上传文件");
        }
        int error = 0;
        for (MultipartFile file : files) {
            try {
                documentService.uploadDocument(new DocumentService.UploadRequest(datasetId,
                        Objects.requireNonNull(file.getOriginalFilename()),
                        Objects.requireNonNull(file.getContentType()),
                        file.getSize(), file.getInputStream()));
            } catch (NexflyException e) {
                error ++;
            }
        }

        return RestResultUtils.success(error);
    }

    @PostMapping("process")
    public RestResult<Boolean> processDocument(@RequestBody DocumentService.AnalysisRequest analysisRequest) throws Exception {
        documentService.processDocument(analysisRequest);
        return RestResultUtils.success(true);
    }

    @GetMapping("list")
    public Object list(@RequestParam("datasetId") Long datasetId, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        PageInfo<Object> documentPage = PageHelper.startPage(page, pageSize).doSelectPageInfo(() -> documentService.list(datasetId));
        return RestResultUtils.success(documentPage);
    }

}
