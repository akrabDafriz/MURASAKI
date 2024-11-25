package com.arcfit.murasaki.request;
import com.arcfit.murasaki.model.Stats;
import com.arcfit.murasaki.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface BaseApiService {

    @POST("/register")
    Call<Void> registerUser(@Body User user);

    @POST("/login")
    Call<Void> loginUser(@Body User user);

    @PUT("/update")
    Call<Void> editProfile(@Body User user);

    @POST("/increase")
    Call<Void> increaseStats(@Body Stats stats);

    @GET("/")
    Call<Stats> getStats();
}