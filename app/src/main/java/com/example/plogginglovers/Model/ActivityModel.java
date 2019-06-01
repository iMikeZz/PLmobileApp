package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivityModel {
    @SerializedName("data")
    @Expose
    private List<ActivityTeam> data = null;

    public List<ActivityTeam> getData() {
        return data;
    }

    public void setData(List<ActivityTeam> data) {
        this.data = data;
    }
}
