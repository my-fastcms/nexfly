package com.nexfly.api.system.bean;

import java.util.List;

/**
 * @Author wangjun
 * @Date 2024/9/26
 **/
public class AppEditResponse extends AppSaveRequest {

    Long orgId;

    List<String> datasetNames;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public List<String> getDatasetNames() {
        return datasetNames;
    }

    public void setDatasetNames(List<String> datasetNames) {
        this.datasetNames = datasetNames;
    }

}
