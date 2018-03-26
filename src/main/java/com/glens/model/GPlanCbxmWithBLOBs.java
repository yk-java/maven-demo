package com.glens.model;

public class GPlanCbxmWithBLOBs extends GPlanCbxm {
    private String olddata;

    private String nowdata;

    private String problemstatus;

    public String getOlddata() {
        return olddata;
    }

    public void setOlddata(String olddata) {
        this.olddata = olddata;
    }

    public String getNowdata() {
        return nowdata;
    }

    public void setNowdata(String nowdata) {
        this.nowdata = nowdata;
    }

    public String getProblemstatus() {
        return problemstatus;
    }

    public void setProblemstatus(String problemstatus) {
        this.problemstatus = problemstatus;
    }
}