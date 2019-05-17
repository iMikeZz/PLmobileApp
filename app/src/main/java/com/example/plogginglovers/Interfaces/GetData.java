package com.example.plogginglovers.Interfaces;

import com.example.plogginglovers.Model.ContactList;
import com.example.plogginglovers.Model.EcopontosList;
import com.example.plogginglovers.Model.LoginToken;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetData {

    @GET("me")
    //Wrap the response in a Call object with the type of the expected result//
    Call<UserData> getStudentInfo(@Header("Authorization") String authHeader);

    @GET("ecopontos")
    Call<EcopontosList> getAllEcopontos(@Header("Authorization") String authHeader);

    @GET("emergency-contacts")
    Call<ContactList> getAllNumbers();

    @FormUrlEncoded
    @POST("login")
    Call<LoginToken> login(@Field("email") String email, @Field("password") String password);

    @POST("logout")
    Call<LogoutToken> logout(@Header("Authorization") String authHeader);
}
