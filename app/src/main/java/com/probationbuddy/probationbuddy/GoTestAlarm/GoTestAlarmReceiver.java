package com.probationbuddy.probationbuddy.gotestalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class GoTestAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 333;

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, GoTestAlarmService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}