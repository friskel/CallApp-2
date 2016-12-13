package com.probationbuddy.probationbuddy.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

////////  This service will clear the current notifications in the status bar from this app

public class HideNotificationService extends IntentService {

    public HideNotificationService() {
        super("HideNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        mNotificationManager.cancel(1234567);
    }
}