package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AchievementModel {
    @SerializedName("data")
    @Expose
    private List<Achievement> data = null;

    public List<Achievement> getData() {
        return data;
    }

    public void setData(List<Achievement> data) {
        this.data = data;
    }
}
