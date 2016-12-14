package com.probationbuddy.probationbuddy.DayAlarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.preference.PreferenceManager;

import com.probationbuddy.probationbuddy.MorningAlarm.MorningService;

public class DayAlarmOnBoot extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean dayAlarmActive = sharedPrefs.getBoolean("dayAlarmRunning", true);


        if (dayAlarmActive){

            Intent startServiceIntent = new Intent(context, MorningService.class);
            startWakefulService(context, startServiceIntent);

        }

    }
}
