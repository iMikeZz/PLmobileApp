package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityModel {
    @SerializedName("data")
    @Expose
    private Activity data;

    public Activity getData() {
        return data;
    }

    public void setData(Activity data) {
        this.data = data;
    }
}
