package com.complete.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class Notification_reciever extends BroadcastReceiver {
    String id="ch1";
    String describe="test";
    int importencce=NotificationManager.IMPORTANCE_LOW;


    //@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        int times=intent.getIntExtra("times",0);
        String Title=intent.getStringExtra("Title");
        if(Build.VERSION.SDK_INT>=26){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent repeat_intent = new Intent(context,MainActivity.class);

            repeat_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,times,repeat_intent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationChannel notificationChannel=new NotificationChannel(id,describe,importencce);
            notificationManager.createNotificationChannel(notificationChannel);
            Notification.Builder builder = new Notification.Builder(context,id)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setContentTitle(Title)
                    .setAutoCancel(true);
            notificationManager.notify(times,builder.build());
        }else{
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent repeat_intent = new Intent(context,MainActivity.class);
            repeat_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,times,repeat_intent,PendingIntent.FLAG_UPDATE_CURRENT);
            //NotificationChannel notificationChannel=new NotificationChannel(id,describe,importencce);
            //notificationManager.createNotificationChannel(notificationChannel);
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setContentTitle(Title)
                    .setAutoCancel(true);
            notificationManager.notify(times,builder.build());
        }

    }
}
