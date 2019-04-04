package com.example.plogginglovers.Model;

import com.google.gson.annotations.SerializedName;

public class Ecoponto {

    @SerializedName("lat")
    private double lat;

    @SerializedName("lon")
    private double lon;

    public Ecoponto(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
