package com.technoidtintin.android.justdoit.Worker;

import android.content.Context;

public class ReminderTask {

    public static final String ACTION_CANCEL_NOTIFICATION = "cancel notification";

    public static void executeTask(Context context,String action){
        if (ACTION_CANCEL_NOTIFICATION.equals(action)){
            NotificationUtils.clearAllNotifications(context);
        }
    }

}
