package com.mitlab.iotdemo_jwt;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mitlab.iotdemo_jwt.network.ApiClient;
import com.mitlab.iotdemo_jwt.network.ApiInterface;
import com.mitlab.iotdemo_jwt.utils.SharedPrefManager;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements PositionServiceCallback {
    private SharedPrefManager sharedPrefManager;
    private ApiInterface apiInterface;
    private Boolean isLogin, isCoordSet;

    private TextView nameText;
    private EditText coordXText, coordYText;
    private Button setCoordBtn, logoutBtn, checkBtn;

    private int coordX, coordY;
    private PositionService positionService;
    boolean mBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = (TextView)findViewById(R.id.nameText);
        setCoordBtn = (Button)findViewById(R.id.setCoordBtn);
        logoutBtn = (Button)findViewById(R.id.btnLogout);
        checkBtn = (Button)findViewById(R.id.btnCheck);
        coordXText = (EditText)findViewById(R.id.coordXText);
        coordYText = (EditText)findViewById(R.id.coordYText);

        isLogin = false;
        isCoordSet = false;
        sharedPrefManager = new SharedPrefManager(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        coordX = coordY = 0;
    }

    public void checkAll(){
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

                doBindService();
                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sharedPrefManager.resetSP();
                        doUnbindService();

                        startActivity(new Intent(MainActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });
                setCoordBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String coordXVal = coordXText.getText().toString();
                        String coordYVal = coordYText.getText().toString();
                        int coordX = Integer.parseInt(coordXVal);
                        int coordY = Integer.parseInt(coordYVal);
                        positionService.setCoord(coordX, coordY);
                        if(!isCoordSet){
                            isCoordSet = true;
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAll();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        doUnbindService();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PositionService.PositionBinder binder = (PositionService.PositionBinder) service;
            positionService = binder.getService();
            positionService.setCallbacks(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void setServiceCoord() {
        if (mBound) {
            String coordXVal = coordXText.getText().toString();
            String coordYVal = coordYText.getText().toString();
            try{
                coordX = Integer.parseInt(coordXVal);
            }catch(NumberFormatException ex) {
                coordX = coordX;
            }
            try{
                coordY = Integer.parseInt(coordYVal);
            }catch(NumberFormatException ex) {
                coordY = coordY;
            }
            positionService.setCoord(coordX, coordY);
        }
    }

    void doBindService() {
        // Attempts to establish a connection with the service.  We use an
        // explicit class name because we want a specific service
        // implementation that we know will be running in our own process
        // (and thus won't be supporting component replacement by other
        // applications).

        if (bindService(new Intent(MainActivity.this, PositionService.class),
                mConnection, Context.BIND_AUTO_CREATE)) {
            mBound = true;
        } else {
            Log.e("MY_APP_TAG", "Error: The requested service doesn't " +
                    "exist, or this client isn't allowed access to it.");
        }
    }

    void doUnbindService() {
        if (mBound) {
            // Release information about the service's state.
            positionService.setCallbacks(null);
            unbindService(mConnection);
            mBound = false;
        }
    }
}