package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ranking {
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("team_name")
    @Expose
    private String teamName;
    @SerializedName("points")
    @Expose
    private String points;
    @SerializedName("photo_url")
    @Expose
    private Object photoUrl;
    @SerializedName("student_team")
    @Expose
    private Boolean studentTeam;

    public Boolean getStudentTeam() {
        return studentTeam;
    }

    public void setStudentTeam(Boolean studentTeam) {
        this.studentTeam = studentTeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Object getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Object photoUrl) {
        this.photoUrl = photoUrl;
    }
}
