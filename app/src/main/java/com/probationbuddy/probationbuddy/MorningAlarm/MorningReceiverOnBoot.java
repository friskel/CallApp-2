package com.probationbuddy.probationbuddy.morningalarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

// WakefulBroadcastReceiver ensures the device does not go back to sleep
// during the startup of the service
public class MorningReceiverOnBoot extends WakefulBroadcastReceiver {
    SharedPreferences sharedPrefs;
    boolean alarmIsActive;
    Intent startServiceIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        alarmIsActive = sharedPrefs.getBoolean("prefsActivate", true);
        startServiceIntent = new Intent(context, MorningServiceStarter.class);

        if (alarmIsActive) {
            Log.i("BOOTY", "MorningReceiverOnBoot is active");
            startWakefulService(context, startServiceIntent);
        }
    }
}


// on boot start MorningServiceStarter service to reset morningalarm