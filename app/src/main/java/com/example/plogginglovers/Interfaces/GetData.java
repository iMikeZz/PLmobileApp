package com.example.plogginglovers.Interfaces;

import com.example.plogginglovers.Model.Contact;
import com.example.plogginglovers.Model.Ecoponto;
import com.example.plogginglovers.Model.LoginToken;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.RetroUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetData {

    @GET("users")
    //Wrap the response in a Call object with the type of the expected result//
    Call<List<RetroUser>> getAllUsers();

    @GET("ecopontos")
    Call<List<Ecoponto>> getAllEcopontos();

    @GET("numbers")
    Call<List<Contact>> getAllNumbers();

    @FormUrlEncoded
    @POST("login")
    Call<LoginToken> login(@Field("email") String email, @Field("password") String password);

    @POST("logout")
    Call<LogoutToken> logout(@Header("Authorization") String authHeader);
}
