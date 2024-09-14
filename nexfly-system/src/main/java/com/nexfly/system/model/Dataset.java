package com.nexfly.system.model;

import com.nexfly.common.model.BaseModel;

import java.io.Serializable;

public class Dataset extends BaseModel implements Serializable {
    private Long datasetId;

    private Long orgId;

    private String name;

    private String description;

    private Long embedModelId;

    private String vsIndexNodeId;

    private static final long serialVersionUID = 1L;

    public Long getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Long datasetId) {
        this.datasetId = datasetId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEmbedModelId() {
        return embedModelId;
    }

    public void setEmbedModelId(Long embedModelId) {
        this.embedModelId = embedModelId;
    }

    public String getVsIndexNodeId() {
        return vsIndexNodeId;
    }

    public void setVsIndexNodeId(String vsIndexNodeId) {
        this.vsIndexNodeId = vsIndexNodeId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", datasetId=").append(datasetId);
        sb.append(", orgId=").append(orgId);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", embedModelId=").append(embedModelId);
        sb.append(", vsIndexNodeId=").append(vsIndexNodeId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}