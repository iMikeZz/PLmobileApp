package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ActivityTeam {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("activities")
    @Expose
    private ArrayList<Activity> activities = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}
