package com.example.plogginglovers.Model;

public class Rubbish {

    private String name;
    private int image;
    private int score;
    private int quantity;

    public Rubbish(String name, int image, int score) {
        this.name = name;
        this.image = image;
        this.score = score;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
