package com.arcfit.murasaki.request;
import com.arcfit.murasaki.model.Aspects;
import com.arcfit.murasaki.model.BaseResponse;
import com.arcfit.murasaki.model.Plans;
import com.arcfit.murasaki.model.Stats;
import com.arcfit.murasaki.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("/user/register")
    Call<BaseResponse<String>> registerUser(
        @Field("username") String username,
        @Field("email") String email,
        @Field("password") String password
    );
    @FormUrlEncoded
    @POST("/user/login")
    Call<BaseResponse<User>> loginUser(
        @Field("email") String email,
        @Field("password") String password
    );

    @PUT("/update")
    Call<User> editProfile(@Query("userId") int userId, @Body User user);

    @FormUrlEncoded
    @POST("/stats/increase")
    Call<BaseResponse<Void>> increaseStats(
            @Field("user_id") String userId,
            @Field("workout_name") String workoutName,
            @Field("sets_number") Integer setsNumber
    );

    @FormUrlEncoded
    @POST("/stats/")
    Call<BaseResponse<Stats>> getStats(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("/stats/aspects")
    Call<BaseResponse<Aspects>> getAspects(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("/plans/add")
    Call<BaseResponse<Plans>> getPlans(
            @Field("user_id") String userId,
            @Field("exercise") String exerciseType,
            @Field("deadline") String deadline
    );


//    @FormUrlEncoded
//    @POST("/stats/increase")
//    Call<BaseResponse<Void>> increaseStats(
//        @Query("user_id") String userId,
//        @Field("aspect_to_change") String aspectToChange,
//        @Field("sets_number") int setsNumber
//    );
}