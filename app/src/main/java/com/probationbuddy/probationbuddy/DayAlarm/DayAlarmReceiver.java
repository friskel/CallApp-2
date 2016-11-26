package com.probationbuddy.probationbuddy.DayAlarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DayAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 222;

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, DayAlarmService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}