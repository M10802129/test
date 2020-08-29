package com.mitlab.iotdemo_jwt.network.interceptor;

import android.content.Intent;

import com.mitlab.iotdemo_jwt.LoginActivity;
import com.mitlab.iotdemo_jwt.MyApp;
import com.mitlab.iotdemo_jwt.network.ApiClient;
import com.mitlab.iotdemo_jwt.network.ApiInterface;
import com.mitlab.iotdemo_jwt.network.response.UserResponse;
import com.mitlab.iotdemo_jwt.utils.SharedPrefManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenAuthenticator implements Interceptor {

    SharedPrefManager sharedPrefManager;

    public TokenAuthenticator() {

        sharedPrefManager = new SharedPrefManager(MyApp.getContext());
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response mainResponse = chain.proceed(chain.request());
        Request mainRequest = chain.request();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if ( mainResponse.code() == 401 || mainResponse.code() == 403 ) {
            String token = sharedPrefManager.getSPToken();
            retrofit2.Response<UserResponse> refreshToken = apiInterface.refreshToken(token).execute();
            if (refreshToken.isSuccessful()) {
                sharedPrefManager.saveSPString(SharedPrefManager.SP_TOKEN, "Bearer " +
                                                refreshToken.body().getToken());
                Request.Builder builder = mainRequest.newBuilder().header("Authorization",
                        sharedPrefManager.getSPToken())
                        .method(mainRequest.method(), mainRequest.body());
                mainResponse = chain.proceed(builder.build());
            }

            //Jika tidak ingin refresh token dan langsung logout
//            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
//            Intent i = new Intent(MyApp.getContext(), LoginActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            MyApp.getContext().startActivity(i);

        } else if ( mainResponse.code() == 500 ){
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_IS_LOGIN, false);
            Intent i = new Intent(MyApp.getContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApp.getContext().startActivity(i);
        }

        return mainResponse;
    }
}
