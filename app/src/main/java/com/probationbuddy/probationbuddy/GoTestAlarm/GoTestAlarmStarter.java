package com.probationbuddy.probationbuddy.gotestalarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;


public class GoTestAlarmStarter extends IntentService {

    SharedPreferences sharedPrefs;
    String intervalPrefString;
    long firstMillis;
    long intervalPref;
    long interval;

    public GoTestAlarmStarter() {
        super("GoTestAlarmStarter");
    }


    @Override
    protected void onHandleIntent(Intent intent) {



        //get shared prefs
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);



        //get interval (15,30, or 60) int from sharedprefs
        intervalPrefString = sharedPrefs.getString("prefInterval", "15");
        intervalPref = Integer.parseInt(intervalPrefString);

        //set interval to correct AlarmManager interval
        interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        if (intervalPref == 15){
            interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        }
        if (intervalPref == 30){
            interval = AlarmManager.INTERVAL_HALF_HOUR;
        }
        if (intervalPref == 60){
            interval = AlarmManager.INTERVAL_HOUR;
        }


        // This runs if you need to test today

        //makes reminder alarm, starts right away and repeats

        // Construct an intent that will execute the AlarmReceiver
        Intent intentGoTestAlarmStart = new Intent(getApplicationContext(), GoTestAlarmReceiver.class);

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, GoTestAlarmReceiver.REQUEST_CODE,
                intentGoTestAlarmStart, PendingIntent.FLAG_UPDATE_CURRENT);

        // alarm is set right away
        firstMillis = System.currentTimeMillis();

        //make alarm
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        //start it
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis, interval, pIntent);
        Log.i("Go Test Service:", " running");

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("goTestAlarmRunning", true);
        editor.apply();

        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }
}