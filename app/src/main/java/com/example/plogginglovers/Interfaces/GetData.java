package com.example.plogginglovers.Interfaces;

import com.example.plogginglovers.Model.Activity;
import com.example.plogginglovers.Model.ActivitiesModel;
import com.example.plogginglovers.Model.ActivityModel;
import com.example.plogginglovers.Model.Captain;
import com.example.plogginglovers.Model.ContactList;
import com.example.plogginglovers.Model.EcopontosList;
import com.example.plogginglovers.Model.Errors;
import com.example.plogginglovers.Model.InfoModel;
import com.example.plogginglovers.Model.Item;
import com.example.plogginglovers.Model.LoginToken;
import com.example.plogginglovers.Model.LogoutToken;
import com.example.plogginglovers.Model.Password;
import com.example.plogginglovers.Model.RubbishModel;
import com.example.plogginglovers.Model.UserData;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface GetData {

    @GET("me")
    //Wrap the response in a Call object with the type of the expected result//
    Call<UserData> getStudentInfo(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("password/create")
    Call<ResponseBody> resetPassword(@Field("email") String email);

    @GET("ecopontos")
    Call<EcopontosList> getAllEcopontos(@Header("Authorization") String authHeader);

    @GET("emergency-contacts")
    Call<ContactList> getAllNumbers();

    @FormUrlEncoded
    @POST("login")
    Call<LoginToken> login(@Field("email") String email, @Field("password") String password);

    @POST("logout")
    Call<LogoutToken> logout(@Header("Authorization") String authHeader);

    @PATCH("me/password")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<Errors> changePassword(@Header("Authorization") String authHeader, @Body Password password);

    @GET("student/activities")
    Call<ActivitiesModel> getStudentActivities(@Header("Authorization") String authHeader);

    @GET("activities/{activity}/items")
    Call<RubbishModel> getActivityItems(@Header("Authorization") String authHeader, @Path("activity") int activity_id);

    @Multipart
    @POST("me/photo")
    Call<ResponseBody> uploadProfilePicture(@Header("Authorization") String authHeader, @Part MultipartBody.Part file);

    @GET("student/teams/{team}/captain")
    Call<Captain> isStudentTeamCaptain(@Header("Authorization") String authHeader, @Path("team") int team_id);

    @Multipart
    @POST("activities/{activity}/{team}/status")
    Call<ResponseBody> updateActivityTeamStatus(@Header("Authorization") String authHeader, @Path("activity") int activity_id, @Path("team") int team_id, @Part MultipartBody.Part file, @Part("status") RequestBody status);

    @GET("activities/{activity}/{team}/status")
    Call<ActivityModel> getActivityTeamStatus(@Header("Authorization") String authHeader, @Path("activity") int activity_id, @Path("team") int team_id);

    @GET("activities/{activity}/{team}/info")
    Call<InfoModel> checkStudentActivityGameInfo(@Header("Authorization") String authHeader, @Path("activity") int activity_id, @Path("team") int team_id);

    @POST("activities/{activity}/{team}/info")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<ResponseBody> newStudentActivityGameInfo(@Header("Authorization") String authHeader, @Path("activity") int activity_id, @Path("team") int team_id, @Body JsonObject items);
}
