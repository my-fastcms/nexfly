package com.nexfly.system.controller;

import com.github.pagehelper.PageInfo;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.common.core.utils.UuidUtil;
import com.nexfly.system.service.DocumentService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
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
    public RestResult<PageInfo<DocumentService.DocumentResponse>> list(@RequestParam("datasetId") Long datasetId, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        return RestResultUtils.success(documentService.list(datasetId, page, pageSize));
    }

    @PostMapping("change/status")
    public RestResult<Boolean> changeStatus(@RequestBody DocumentService.ChangeStatusRequest changeStatusRequest) throws NexflyException {
        documentService.changeStatus(changeStatusRequest);
        return RestResultUtils.success(true);
    }

    @PostMapping("rename")
    public RestResult<Boolean> rename(@RequestBody DocumentService.RenameRequest renameRequest) throws NexflyException {
        documentService.renameDocument(renameRequest);
        return RestResultUtils.success(true);
    }

    @PostMapping("delete")
    public RestResult<Boolean> delete(@RequestBody List<Long> documentIds) {
        documentService.delete(documentIds);
        return RestResultUtils.success(true);
    }

    @GetMapping("download")
    public void download(@RequestParam("documentId") Long documentId, HttpServletResponse response) throws Exception {
        response.reset();
        InputStream inputStream = documentService.downloadDocument(documentId);
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(UuidUtil.getSimpleUuid(), "UTF-8"));
        byte[] bytes = new byte[1024];
        int len;
        while ((len = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, len);
        }
        outputStream.close();
        inputStream.close();
    }

}
