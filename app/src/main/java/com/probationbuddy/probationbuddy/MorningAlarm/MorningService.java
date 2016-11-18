package com.probationbuddy.probationbuddy.MorningAlarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.probationbuddy.probationbuddy.DayAlarm.DayAlarmReceiver;


public class MorningService extends IntentService {
    long interval;
    long firstMillis;
    public MorningService() {
        super("MorningService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        String dayAlarmInterval = prefs.getString("intervalPref", "stringdef"); //0 is the default value.


        interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;



        // This runs at the set time every morning (morningAlarm)

        //makes reminder alarm, starts right away and repeats

        // Construct an intent that will execute the AlarmReceiver
        Intent intentDayAlarmStart = new Intent(getApplicationContext(), DayAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, DayAlarmReceiver.REQUEST_CODE,
                intentDayAlarmStart, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, interval, pIntent);
        Log.i("Morning Service:", " running");
    }
}