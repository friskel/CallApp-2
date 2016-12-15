package com.probationbuddy.probationbuddy.DayAlarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.probationbuddy.probationbuddy.MorningAlarm.MorningService;

public class DayAlarmOnBoot extends WakefulBroadcastReceiver {
    SharedPreferences sharedPrefs;
    boolean dayAlarmActive;
    Intent startServiceIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        dayAlarmActive = sharedPrefs.getBoolean("dayAlarmRunning", true);
        startServiceIntent = new Intent(context, MorningService.class);

        if (dayAlarmActive){

            Log.i("BOOTY", "DayAlarmReceiverOnBoot is active");
            startWakefulService(context, startServiceIntent);

        }

    }
}
