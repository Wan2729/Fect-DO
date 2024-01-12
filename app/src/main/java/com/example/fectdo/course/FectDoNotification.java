package com.example.fectdo.course;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.example.fectdo.R;

public class FectDoNotification extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("TRUE".equals(action)) {
            // Handle "Yes" action
            // You can retrieve extras if you passed any
            // String value = intent.getStringExtra("your_key");
            Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();

        } else if ("FALSE".equals(action)) {
            // Handle "No" action
            Toast.makeText(context, "No", Toast.LENGTH_SHORT).show();

        }
        clearNotification(context);
    }

    private void clearNotification(Context context) {
        // Clear the notification by its ID
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(200);
    }
}
