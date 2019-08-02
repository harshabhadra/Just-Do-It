package com.example.android.justdoit;

import android.app.IntentService;
import android.content.Intent;

import com.example.android.justdoit.Worker.ReminderTask;

public class NotficationIntentService extends IntentService {

    public NotficationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String action = intent.getAction();
        ReminderTask.executeTask(this, action);
    }
}
