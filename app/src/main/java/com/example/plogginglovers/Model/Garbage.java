package com.example.plogginglovers.Model;

public class Garbage {

    private String garbage;

    private String ecoponto;

    public Garbage(String garbage, String ecoponto) {
        this.garbage = garbage;
        this.ecoponto = ecoponto;
    }

    public String getGarbage() {
        return garbage;
    }

    public void setGarbage(String garbage) {
        this.garbage = garbage;
    }

    public String getEcoponto() {
        return ecoponto;
    }

    public void setEcoponto(String ecoponto) {
        this.ecoponto = ecoponto;
    }

    @Override
    public String toString() {
        return garbage;
    }
}
