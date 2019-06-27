package com.example.plogginglovers.Model;

public class Statistic {

    private int image;
    private String title;
    private int quantity;

    public Statistic(int image, String title, int quantity) {
        this.image = image;
        this.title = title;
        this.quantity = quantity;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
