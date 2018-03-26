package com.glens.model;

import java.util.Date;

public class AttachmentInfo {
    private Integer attachId;

    private String attachSavePath;

    private String attachSaveName;

    private Date attachSaveTime;

    private String attachType;

    public Integer getAttachId() {
        return attachId;
    }

    public void setAttachId(Integer attachId) {
        this.attachId = attachId;
    }

    public String getAttachSavePath() {
        return attachSavePath;
    }

    public void setAttachSavePath(String attachSavePath) {
        this.attachSavePath = attachSavePath;
    }

    public String getAttachSaveName() {
        return attachSaveName;
    }

    public void setAttachSaveName(String attachSaveName) {
        this.attachSaveName = attachSaveName;
    }

    public Date getAttachSaveTime() {
        return attachSaveTime;
    }

    public void setAttachSaveTime(Date attachSaveTime) {
        this.attachSaveTime = attachSaveTime;
    }

    public String getAttachType() {
        return attachType;
    }

    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }
}