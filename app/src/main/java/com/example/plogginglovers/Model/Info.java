package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {
    @SerializedName("captain")
    @Expose
    private Boolean captain;
    @SerializedName("studentSend")
    @Expose
    private Boolean studentSend;
    @SerializedName("teamReady")
    @Expose
    private Boolean teamReady;

    public Boolean getCaptain() {
        return captain;
    }

    public void setCaptain(Boolean captain) {
        this.captain = captain;
    }

    public Boolean getStudentSend() {
        return studentSend;
    }

    public void setStudentSend(Boolean studentSend) {
        this.studentSend = studentSend;
    }

    public Boolean getTeamReady() {
        return teamReady;
    }

    public void setTeamReady(Boolean teamReady) {
        this.teamReady = teamReady;
    }
}
