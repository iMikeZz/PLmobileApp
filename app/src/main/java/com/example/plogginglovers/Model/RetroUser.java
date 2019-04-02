package com.example.plogginglovers.Model;

import com.google.gson.annotations.SerializedName;

public class RetroUser {

    @SerializedName("name")
    private String name;

    public RetroUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
