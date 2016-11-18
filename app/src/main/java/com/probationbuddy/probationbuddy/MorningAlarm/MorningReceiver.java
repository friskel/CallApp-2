package com.probationbuddy.probationbuddy.MorningAlarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MorningReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 111;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MorningService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
        //stackoverflow:  use this or other way of starting service from broadcast?
    }
}


// broadcast receiver that is fired from morningAlarm every morning at the set time

// starts the MorningService service