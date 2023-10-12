package com.example.oa.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.oa.R;

import java.util.Random;

public class NotificationHelper {

    private static final String CHANNEL_ID = "your_channel_id";
    private static final String CHANNEL_NAME = "Your Channel Name";
    private static final String CHANNEL_DESCRIPTION = "Your Channel Description";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // 创建通知渠道
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            // 注册通知渠道
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void sendNotification(Context context, String title, String message) {



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        long notificationId = System.currentTimeMillis();
        notificationManager.notify((int) notificationId, builder.build());
        notificationManager.notify((int) notificationId, builder.build());
    }
}
