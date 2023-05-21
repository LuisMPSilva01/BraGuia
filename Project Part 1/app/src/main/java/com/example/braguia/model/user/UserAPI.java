package com.example.braguia.model.user;

import com.example.braguia.model.trails.Trail;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserAPI {
    @GET("/user")
    Call<User> getUser(@Header("Cookie") String cookie);
    @POST("login")
    Call<User> login(@Body JsonObject login) throws IOException;

    @POST("logout")
    Call<User> logout(@Header("Content-Range") String contentRange);
}
