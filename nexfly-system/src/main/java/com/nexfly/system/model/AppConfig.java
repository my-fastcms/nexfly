package com.nexfly.system.model;

import com.nexfly.common.model.BaseModel;
import java.io.Serializable;

public class AppConfig extends BaseModel implements Serializable {
    private Long configId;

    private Long appId;

    private Long orgId;

    private String prePrompt;

    private String formVariable;

    private String fileUpload;

    private String textToSpeech;

    private String speechToText;

    private static final long serialVersionUID = 1L;

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getPrePrompt() {
        return prePrompt;
    }

    public void setPrePrompt(String prePrompt) {
        this.prePrompt = prePrompt;
    }

    public String getFormVariable() {
        return formVariable;
    }

    public void setFormVariable(String formVariable) {
        this.formVariable = formVariable;
    }

    public String getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(String fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getTextToSpeech() {
        return textToSpeech;
    }

    public void setTextToSpeech(String textToSpeech) {
        this.textToSpeech = textToSpeech;
    }

    public String getSpeechToText() {
        return speechToText;
    }

    public void setSpeechToText(String speechToText) {
        this.speechToText = speechToText;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", configId=").append(configId);
        sb.append(", appId=").append(appId);
        sb.append(", orgId=").append(orgId);
        sb.append(", prePrompt=").append(prePrompt);
        sb.append(", formVariable=").append(formVariable);
        sb.append(", fileUpload=").append(fileUpload);
        sb.append(", textToSpeech=").append(textToSpeech);
        sb.append(", speechToText=").append(speechToText);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}