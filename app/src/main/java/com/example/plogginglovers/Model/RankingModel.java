package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RankingModel {
    @SerializedName("data")
    @Expose
    private RankingData data;

    public RankingData getData() {
        return data;
    }

    public void setData(RankingData data) {
        this.data = data;
    }
}
