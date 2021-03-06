package com.mitlab.iotdemo_jwt.network;

import com.mitlab.iotdemo_jwt.network.response.PositionSetResponse;
import com.mitlab.iotdemo_jwt.network.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("api/auth/logindevice")
    Call<UserResponse> login(@Field("email") String email,
                             @Field("password") String password,
                             @Field("device_token") String device_token);

    @POST("api/auth/refresh")
    Call<UserResponse> refreshToken(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/worker/position/set")
    Call<PositionSetResponse> setPostion(@Header("Authorization") String token,
                                         @Field("device_token") String deviceToken,
                                         @Field("pos_x") int posX,
                                         @Field("pos_y") int posY);
}
