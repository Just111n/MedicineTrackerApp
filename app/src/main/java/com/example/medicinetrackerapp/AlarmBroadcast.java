package com.example.medicinetrackerapp;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

public class AlarmBroadcast extends BroadcastReceiver {

    public final static String Event_KEY = "EVENT_KEY";
    public final static String TIME_KEY = "TIME_KEY";
    public final static String NOTI_KEY = "NOTI_KEY";

    public final static String DOSAGE_KEY = "DOSAGE_KEY";

    String CHANNEL_ID = "notify_001";

    final static String NOTI = "ALARMTAG";




    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String text = bundle.getString(Event_KEY);
        String time = bundle.getString(TIME_KEY);
        String medDosage = bundle.getString(DOSAGE_KEY);
        int notiID = bundle.getInt(NOTI_KEY);
        Log.d(NOTI,"=========================");
        Log.d(NOTI,"AlarmBroadcast received time:"+time);
        Log.d(NOTI,text);



        //Click on Notification
        Intent lauchMainActivityIntent = new Intent(context, MainActivity.class);
        lauchMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Notification Builder
        // PendingIntent should be used only once. If the user interacts with the notification, the PendingIntent will be deleted and subsequent attempts to use it will fail.
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, lauchMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);


        //here we set all the properties for the notification
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.message, text);
        contentView.setTextViewText(R.id.date, time);
        contentView.setTextViewText(R.id.dosage_textView,medDosage);
        mBuilder.setSmallIcon(R.drawable.alarm);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        //we have to create notification channel after api level 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "channel name";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(CHANNEL_ID);
        }

        Notification notification = mBuilder.build();
        int notificationId = 1;
        Log.d(NOTI,"notification ID: :"+notiID);
        notificationManager.notify(notiID, notification);
//        notificationManager.notify(notificationId, notification);


    }
}

