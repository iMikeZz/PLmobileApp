package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("activity_item_id")
    @Expose
    private Integer activity_item_id;
    @SerializedName("item_quantity")
    @Expose
    private Integer item_quantity;
    @SerializedName("student_score")
    @Expose
    private Integer student_score;

    public Item(Integer activity_item_id, Integer item_quantity, Integer student_score) {
        this.activity_item_id = activity_item_id;
        this.item_quantity = item_quantity;
        this.student_score = student_score;
    }

    public Integer getActivity_item_id() {
        return activity_item_id;
    }

    public void setActivity_item_id(Integer activity_item_id) {
        this.activity_item_id = activity_item_id;
    }

    public Integer getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(Integer item_quantity) {
        this.item_quantity = item_quantity;
    }

    public Integer getStudent_score() {
        return student_score;
    }

    public void setStudent_score(Integer student_score) {
        this.student_score = student_score;
    }
}
