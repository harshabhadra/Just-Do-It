package com.technoidtintin.android.justdoit.Worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    private static final String TAG = NotificationWorker.class.getSimpleName();


    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String message = getInputData().getString("message");
        Context context = getApplicationContext();
        NotificationUtils.makeNotification("Your Task: " + message, context);
        return Result.success();
    }
}
