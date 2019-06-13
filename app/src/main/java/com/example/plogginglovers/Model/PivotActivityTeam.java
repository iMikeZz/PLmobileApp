package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PivotActivityTeam {
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("activity_id")
    @Expose
    private Integer activityId;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
