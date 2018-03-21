package com.example.nomanikram.epilepsyseizuredetection;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.nomanikram.epilepsyseizuredetection.MainActivity;
import com.example.nomanikram.epilepsyseizuredetection.R;

/**
 * Created by nomanikram on 16/03/2018.
 */

public class MyBluetoothService extends Service {



    @Override
    public void onCreate() {
        super.onCreate();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            Intent notificationIntent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Epilipsy Monitoring System")
                    .setContentText("Doing some work...")
                    .setOngoing(true)
                    .setContentIntent(pendingIntent).build();
                    startForeground(1337, notification);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.w("","Service Ended by choice");
    }
}
