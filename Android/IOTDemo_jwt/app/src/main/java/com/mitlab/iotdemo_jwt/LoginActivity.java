package com.mitlab.iotdemo_jwt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.mitlab.iotdemo_jwt.model.User;
import com.mitlab.iotdemo_jwt.model.Worker;
import com.mitlab.iotdemo_jwt.network.ApiClient;
import com.mitlab.iotdemo_jwt.network.ApiInterface;
import com.mitlab.iotdemo_jwt.network.response.UserResponse;
import com.mitlab.iotdemo_jwt.utils.Helper;
import com.mitlab.iotdemo_jwt.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    private Context mContext;
    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mContext = this;

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);

        if (sharedPrefManager.getSPIsLogin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login(){
        progressDialog.show();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        //save to sharedPref
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_DEVICE_TOKEN, token);

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
//                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

                        Call<UserResponse> login = apiInterface.login(etEmail.getText().toString(),
                                                                      etPassword.getText().toString(),
                                                                      token);
                        login.enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                progressDialog.dismiss();
                                if(response.code() == 200){
                                    User user = response.body().getUser();
                                    Worker worker = user.getWorker();

                                    Gson gson = new Gson();
                                    String json = gson.toJson(worker);

                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NAME, worker.getName());
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_WORKER, json);

                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_TOKEN, "bearer " +response.body().getToken());
                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_IS_LOGIN, true);
                                    sharedPrefManager.saveSPLong(SharedPrefManager.SP_EXPIRETIME, Helper.getNowTimestamp()+response.body().getExpires_in());
                                    startActivity(new Intent(mContext, MainActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                }else{
                                    Toast.makeText(mContext, "登入失敗", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                progressDialog.dismiss();

                            }
                        });
                    }
                });
    }

}