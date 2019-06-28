package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticModel {
    @SerializedName("steps")
    @Expose
    private String steps;
    @SerializedName("calories")
    @Expose
    private Integer calories;
    @SerializedName("kilometers")
    @Expose
    private Integer kilometers;
    @SerializedName("objects")
    @Expose
    private String objects;
    @SerializedName("objects_activity_average")
    @Expose
    private Integer objectsActivityAverage;
    @SerializedName("ecopontos")
    @Expose
    private Integer ecopontos;
    @SerializedName("kilometers_global")
    @Expose
    private Integer kilometersGlobal;
    @SerializedName("objects_global")
    @Expose
    private Integer objectsGlobal;
    @SerializedName("schools")
    @Expose
    private Integer schools;
    @SerializedName("users")
    @Expose
    private Integer users;

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public String getObjects() {
        return objects;
    }

    public void setObjects(String objects) {
        this.objects = objects;
    }

    public Integer getObjectsActivityAverage() {
        return objectsActivityAverage;
    }

    public void setObjectsActivityAverage(Integer objectsActivityAverage) {
        this.objectsActivityAverage = objectsActivityAverage;
    }

    public Integer getEcopontos() {
        return ecopontos;
    }

    public void setEcopontos(Integer ecopontos) {
        this.ecopontos = ecopontos;
    }

    public Integer getKilometersGlobal() {
        return kilometersGlobal;
    }

    public void setKilometersGlobal(Integer kilometersGlobal) {
        this.kilometersGlobal = kilometersGlobal;
    }

    public Integer getObjectsGlobal() {
        return objectsGlobal;
    }

    public void setObjectsGlobal(Integer objectsGlobal) {
        this.objectsGlobal = objectsGlobal;
    }

    public Integer getSchools() {
        return schools;
    }

    public void setSchools(Integer schools) {
        this.schools = schools;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }
}
