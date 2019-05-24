package com.example.plogginglovers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorMessages {
    @SerializedName("old_password")
    @Expose
    private List<String> oldPassword = null;
    @SerializedName("password")
    @Expose
    private List<String> password = null;
    @SerializedName("password_confirmation")
    @Expose
    private List<String> passwordConfirmation = null;

    public List<String> getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(List<String> oldPassword) {
        this.oldPassword = oldPassword;
    }

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    public List<String> getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(List<String> passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
