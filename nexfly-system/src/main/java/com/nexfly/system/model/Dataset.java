package com.nexfly.system.model;

import com.nexfly.common.model.BaseModel;

import java.io.Serializable;

public class Dataset extends BaseModel implements Serializable {
    private Long datasetId;

    private Long orgId;

    private String name;

    private String description;

    private String embedModelId;

    private String vsIndexNodeId;

    private String avatar;

    private String language;

    private String parserId;

    private String parserConfig;

    private Integer status;

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

    public String getEmbedModelId() {
        return embedModelId;
    }

    public void setEmbedModelId(String embedModelId) {
        this.embedModelId = embedModelId;
    }

    public String getVsIndexNodeId() {
        return vsIndexNodeId;
    }

    public void setVsIndexNodeId(String vsIndexNodeId) {
        this.vsIndexNodeId = vsIndexNodeId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getParserId() {
        return parserId;
    }

    public void setParserId(String parserId) {
        this.parserId = parserId;
    }

    public String getParserConfig() {
        return parserConfig;
    }

    public void setParserConfig(String parserConfig) {
        this.parserConfig = parserConfig;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProvider() {
        return this.embedModelId.split("@")[0];
    }

    public String getModel() {
        return this.embedModelId.split("@")[1];
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