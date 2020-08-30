package com.mitlab.iotdemo_jwt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.mitlab.iotdemo_jwt.network.ApiClient;
import com.mitlab.iotdemo_jwt.network.ApiInterface;
import com.mitlab.iotdemo_jwt.network.response.PositionSetResponse;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;

public class PositionService extends Service {


    private final IBinder mBinder = new PositionBinder();

    private static Timer timer = new Timer();
    private Context ctx;
    private int coordX, coordY;
    private String token, deviceToken;
    private boolean coordSet = false;

    private int cnt = 0;

    private PositionServiceCallback serviceCallbacks;
    private ApiInterface apiInterface;

    public PositionService() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }


    public class PositionBinder extends Binder{
        PositionService getService(){
            return PositionService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }
    public void onCreate()
    {
        super.onCreate();
        ctx = this;
        startService();
    }

    public void setCallbacks(PositionServiceCallback callbacks) {
        serviceCallbacks = callbacks;
    }

    private void startService()
    {
        if(timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new mainTask(), 0, 10000);
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        timer.cancel();
        timer.purge();
        timer = null;
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
    }

    private final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            cnt += 1;
            if (serviceCallbacks != null) {
                serviceCallbacks.setServiceCoord();
                Call<PositionSetResponse> positionSet = apiInterface.setPostion(token, deviceToken, coordX, coordY);
                positionSet.enqueue(new retrofit2.Callback<PositionSetResponse>() {
                    @Override
                    public void onResponse(Call<PositionSetResponse> call, Response<PositionSetResponse> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getApplicationContext(), "send position api success", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "send position failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PositionSetResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "send position internet failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
//            Toast.makeText(getApplicationContext(), "test cnt" + cnt + ":" + test(), Toast.LENGTH_SHORT).show();
//            Log.e("Position service", "test cnt" + cnt + ":" + test());
        }
    };
    public int setCoord(int X, int Y, String deviceToken, String token){
        coordX = X;
        coordY = Y;
        this.deviceToken = deviceToken;
        this.token = token;
        coordSet = true;
        return coordX + coordY;
    }

    public int test(){
        return coordX + coordY;
    }


}
