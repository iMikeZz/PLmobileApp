package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EcopontosList {
    @SerializedName("data")
    @Expose
    private List<Ecoponto> data = null;

    public List<Ecoponto> getData() {
        return data;
    }

    public void setData(List<Ecoponto> data) {
        this.data = data;
    }
}
