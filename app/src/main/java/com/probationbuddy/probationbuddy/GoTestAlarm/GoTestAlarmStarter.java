package com.probationbuddy.probationbuddy.GoTestAlarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;


public class GoTestAlarmStarter extends IntentService {
    String goTestIntervalString;
    int goTestInterval;
    long firstMillis;
    public GoTestAlarmStarter() {
        super("GoTestAlarmStarter");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


//        goTestInterval = prefs.getInt("goTestInterval", 1); //0 is the default value.
        goTestIntervalString = prefs.getString("goTestInterval", "1");
        goTestInterval = 50000;


        // This runs if you need to test today

        //makes reminder alarm, starts right away and repeats

        // Construct an intent that will execute the AlarmReceiver
        Intent intentGoTestAlarmStart = new Intent(getApplicationContext(), GoTestAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, GoTestAlarmReceiver.REQUEST_CODE,
                intentGoTestAlarmStart, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis, goTestInterval, pIntent);
        Log.i("Go Test Service:", " running");
    }
}