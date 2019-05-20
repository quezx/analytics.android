package com.quezx.analytics.connectivity;

import com.quezx.analytics.model.AccessToken;
import com.quezx.analytics.model.UserModel;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface AnalyticsService {
    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> loginUser(@Field("username") String username,
                                @Field("password") String password,
                                @Field("grant_type") String grant_type,
                                @Field("client_id") String client_id,
                                @Field("client_secret") String client_secret);

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> refreshToken(@Field("grant_type") String grant_type,
                                   @Field("refresh_token") String refresh_token);

    @GET("/api/users/me")
    Call<UserModel> getUserInfo();
}
