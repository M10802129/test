package com.mitlab.iotdemo_jwt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class PositionService extends Service {


    private final IBinder mBinder = new PositionBinder();

    private static Timer timer = new Timer();
    private Context ctx;
    private int coordX, coordY;
    private boolean coordSet = false;

    private int cnt = 0;

    private PositionServiceCallback serviceCallbacks;
    public PositionService() {

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
        timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
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
            }
            Toast.makeText(getApplicationContext(), "test cnt" + cnt + ":" + test(), Toast.LENGTH_SHORT).show();
            Log.e("Position service", "test cnt" + cnt + ":" + test());
        }
    };
    public int setCoord(int X, int Y){
        coordX = X;
        coordY = Y;
        coordSet = true;
        return coordX + coordY;
    }

    public int test(){
        return coordX + coordY;
    }


}
