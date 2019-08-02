package com.example.android.justdoit.Worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.android.justdoit.Activities.MainActivity;
import com.example.android.justdoit.Constants;
import com.example.android.justdoit.NotficationIntentService;
import com.example.android.justdoit.R;

public class NotificationUtils {

    private static final String TAG = NotificationUtils.class.getSimpleName();
    private static final int ACTION_CANCEL_PENDING_INTENT_ID = 1;
    private static final int ACTION_GO_PENDING_INTENT_ID = 2;


    //Cancel notifications
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(Constants.NOTIFICATION_ID);
        }
    }

    //Make a notification
    public static void makeNotification(String message, Context context) {

        //Make a channel if device is running on Android O or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //Channel name, description, importance
            CharSequence name = Constants.NOTIFICATION_CHANNEL_NAME;
            String description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            //Add the channel
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        //Create Body Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_reminder)
                .setContentTitle(Constants.NOTIFICATION_TITLE)
                .setContentText(message)
                .setColor(Color.BLUE)
                .addAction(cancelNoti(context))
                .addAction(letsGoAction(context))
                .setContentIntent(contentIntent(context))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColorized(true);

        //Show the Notification
        NotificationManagerCompat.from(context).notify(Constants.NOTIFICATION_ID, builder.build());

    }

    //create cancel action button
    private static NotificationCompat.Action cancelNoti(Context context) {

        Intent cancelIntent = new Intent(context, NotficationIntentService.class);
        cancelIntent.setAction(ReminderTask.ACTION_CANCEL_NOTIFICATION);

        PendingIntent pendingIntent = PendingIntent.getService(context
                , ACTION_CANCEL_PENDING_INTENT_ID
                , cancelIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Action(R.mipmap.ic_reminder, "Cancel", pendingIntent);
    }

    //create Let's go action button
    private static NotificationCompat.Action letsGoAction(Context context) {

        Intent letsGoIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                ACTION_GO_PENDING_INTENT_ID,
                letsGoIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Action(R.mipmap.ic_reminder, "Let's Go", pendingIntent);

    }

    //Creating Pending Intent
    private static PendingIntent contentIntent(Context context) {
        Intent startContent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                Constants.PENDING_INTENT_ID,
                startContent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
