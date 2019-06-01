package com.example.plogginglovers.Interfaces;

import com.example.plogginglovers.Model.ActivityModel;
import com.example.plogginglovers.Model.ContactList;
import com.example.plogginglovers.Model.EcopontosList;
import com.example.plogginglovers.Model.Errors;
import com.example.plogginglovers.Model.LoginToken;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.UserData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
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

    @FormUrlEncoded
    @PATCH("me/password")
    Call<Errors> changePassword(@Header("Authorization") String authHeader,
                                @Field("old_password") String old_password,
                                @Field("password") String password,
                                @Field("password_confirmation") String password_confirmation);

    @GET("student/activities")
    Call<ActivityModel> getStudentActivities(@Header("Authorization") String authHeader);
}
