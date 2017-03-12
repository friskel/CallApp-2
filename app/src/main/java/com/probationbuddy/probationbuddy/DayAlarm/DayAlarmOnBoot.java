package com.probationbuddy.probationbuddy.dayalarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.probationbuddy.probationbuddy.morningalarm.MorningService;

public class DayAlarmOnBoot extends WakefulBroadcastReceiver {
    SharedPreferences sharedPrefs;
    boolean dayAlarmActive;
    boolean alarmActive;
    Intent startServiceIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        dayAlarmActive = sharedPrefs.getBoolean("dayAlarmRunning", true);
        alarmActive = sharedPrefs.getBoolean("prefsActivate", true);
        startServiceIntent = new Intent(context, MorningService.class);

        if (dayAlarmActive && alarmActive){

            Log.i("BOOTY", "DayAlarmReceiverOnBoot is active");
            startWakefulService(context, startServiceIntent);

        }

    }
}
