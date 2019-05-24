package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Errors {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("errors")
    @Expose
    private ErrorMessages errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorMessages getErrors() {
        return errors;
    }

    public void setErrors(ErrorMessages errors) {
        this.errors = errors;
    }
}
