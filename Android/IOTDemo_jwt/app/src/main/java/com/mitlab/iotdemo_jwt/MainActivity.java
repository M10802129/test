package com.mitlab.iotdemo_jwt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mitlab.iotdemo_jwt.network.ApiClient;
import com.mitlab.iotdemo_jwt.network.ApiInterface;
import com.mitlab.iotdemo_jwt.utils.SharedPrefManager;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private SharedPrefManager sharedPrefManager;
    private ApiInterface apiInterface;
    private Boolean isLogin;

    private TextView nameText;
    private Button logoutBtn, checkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = (TextView)findViewById(R.id.nameText);
        logoutBtn = (Button)findViewById(R.id.btnLogout);
        checkBtn = (Button)findViewById(R.id.btnCheck);

        isLogin = false;
        sharedPrefManager = new SharedPrefManager(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if(!sharedPrefManager.getSPIsLogin()){ //未登入
            isLogin = false;
            Intent intent = new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else{
            if(sharedPrefManager.getLoginExpire()){ //登入到期
                isLogin = false;
                Intent intent = new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }else{
                isLogin = true;
                nameText.setText(sharedPrefManager.getSPName());
                Toast.makeText(this, sharedPrefManager.getWorkerJson(), Toast.LENGTH_LONG).show();
                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sharedPrefManager.resetSP();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });
            }
        }
    }
}