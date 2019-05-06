package com.example.plogginglovers.Model;

public class Achievement {

    private String name;
    private int img;
    private int objectivo;
    private int background;
    private int status; //0 not done //1 done

    public Achievement(String name, int img, int objectivo, int background, int status) {
        this.name = name;
        this.img = img;
        this.objectivo = objectivo;
        this.background = background;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getObjectivo() {
        return objectivo;
    }

    public void setObjectivo(int objectivo) {
        this.objectivo = objectivo;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
