package com.nexfly.system.controller;

import com.nexfly.common.auth.utils.AuthUtils;
import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.model.Dataset;
import com.nexfly.system.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/8/25
 **/
@RestController
@RequestMapping(NexflyConstants.API_PREFIX_MAPPING + "/dataset")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;

    @PostMapping("save")
    public Object save(@RequestBody Dataset dataset) {
        datasetService.save(dataset);
        return RestResultUtils.success();
    }

    @GetMapping("list")
    public RestResult<List<DatasetService.DatasetResponse>> list() {
        return RestResultUtils.success(datasetService.list(AuthUtils.getUserId()));
    }

    @GetMapping("detail")
    public RestResult<Dataset> detail(@RequestParam("datasetId") Long datasetId) {
        return RestResultUtils.success(datasetService.getById(datasetId));
    }

}
