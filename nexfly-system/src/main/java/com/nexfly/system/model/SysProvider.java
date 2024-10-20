package com.nexfly.system.model;

import com.nexfly.common.model.BaseModel;
import java.io.Serializable;

public class SysProvider extends BaseModel implements Serializable {
    private Long sysProviderId;

    private String name;

    private String logo;

    private String tags;

    private Integer status;

    private static final long serialVersionUID = 1L;

    public Long getSysProviderId() {
        return sysProviderId;
    }

    public void setSysProviderId(Long sysProviderId) {
        this.sysProviderId = sysProviderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sysProviderId=").append(sysProviderId);
        sb.append(", name=").append(name);
        sb.append(", logo=").append(logo);
        sb.append(", tags=").append(tags);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}