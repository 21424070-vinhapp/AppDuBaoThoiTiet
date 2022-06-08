package com.example.appdubaothoitiet.presentation.Application;

import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.ID_NOTIFICATION;
import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.NAME_NOTIFICATION;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel=new NotificationChannel(ID_NOTIFICATION,NAME_NOTIFICATION,NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }
}
