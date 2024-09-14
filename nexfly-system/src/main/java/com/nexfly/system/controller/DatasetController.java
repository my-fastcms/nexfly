package com.nexfly.system.controller;

import com.nexfly.common.core.constants.NexflyConstants;
import com.nexfly.common.core.rest.RestResultUtils;
import com.nexfly.system.model.Dataset;
import com.nexfly.system.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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
        String className = "Class" + UUID.randomUUID().toString().replaceAll("-", "");
        dataset.setVsIndexNodeId(className);
        datasetService.save(dataset);
        return RestResultUtils.success();
    }

}
