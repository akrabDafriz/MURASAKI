package com.arcfit.murasaki.request;
import com.arcfit.murasaki.model.BaseResponse;
import com.arcfit.murasaki.model.Stats;
import com.arcfit.murasaki.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("/register")
    Call<BaseResponse<String>> registerUser(
        @Field("username") String username,
        @Field("email") String email,
        @Field("password") String password
    );
    @FormUrlEncoded
    @POST("/login")
    Call<BaseResponse<User>> loginUser(
        @Field("email") String email,
        @Field("password") String password
    );

    @PUT("/update")
    Call<User> editProfile(@Query("userId") int userId, @Body User user);

    @POST("/increase")
    Call<Void> increaseStats(@Body Stats stats);

    @GET("/")
    Call<Stats> getStats();
}