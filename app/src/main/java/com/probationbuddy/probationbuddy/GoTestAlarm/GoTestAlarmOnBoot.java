package com.probationbuddy.probationbuddy.GoTestAlarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.preference.PreferenceManager;

public class GoTestAlarmOnBoot extends WakefulBroadcastReceiver {
    public GoTestAlarmOnBoot() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean goTestAlarmActive = sharedPrefs.getBoolean("goTestAlarmRunning", false);


        if (goTestAlarmActive){

            Intent startServiceIntent = new Intent(context, GoTestAlarmService.class);
            startWakefulService(context, startServiceIntent);

        }


    }
}
