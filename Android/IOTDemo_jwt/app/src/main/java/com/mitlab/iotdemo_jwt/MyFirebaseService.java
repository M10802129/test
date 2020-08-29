package com.mitlab.iotdemo_jwt;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mitlab.iotdemo_jwt.utils.SharedPrefManager;

public class MyFirebaseService extends FirebaseMessagingService {

    private SharedPrefManager sharedPrefManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            Log.i("MyFirebaseService","title "+remoteMessage.getNotification().getTitle());
            Log.i("MyFirebaseService","body "+remoteMessage.getNotification().getBody());
        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("MyFirebaseService","token "+s);
        saveToken(s);
    }

    private void saveToken(final String token) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable(){
            @Override
            public void run() {
                Context context = getApplicationContext();
                SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
                sharedPrefManager.saveSPString(SharedPrefManager.SP_DEVICE_TOKEN, token);
                Toast.makeText(getApplicationContext(), "save token to pref", Toast.LENGTH_LONG).show();
            }
        });
    }

}
