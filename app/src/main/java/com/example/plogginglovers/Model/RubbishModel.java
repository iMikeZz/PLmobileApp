package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RubbishModel {
    @SerializedName("data")
    @Expose
    private List<Rubbish> data = null;

    public List<Rubbish> getData() {
        return data;
    }

    public void setData(List<Rubbish> data) {
        this.data = data;
    }
}
