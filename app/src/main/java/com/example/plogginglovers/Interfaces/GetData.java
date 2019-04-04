package com.example.plogginglovers.Interfaces;

import com.example.plogginglovers.Model.Ecoponto;
import com.example.plogginglovers.Model.RetroUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetData {

    @GET("users")
    //Wrap the response in a Call object with the type of the expected result//
    Call<List<RetroUser>> getAllUsers();

    @GET("ecopontos")
    Call<List<Ecoponto>> getAllEcopontos();
}
