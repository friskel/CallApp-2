package com.probationbuddy.probationbuddy.MorningAlarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;


public class MorningServiceStarter extends IntentService {
    int hour;
    int minute;
    int morningHour;
    int morningMinute;
    public MorningServiceStarter() {
        super("MorningServiceStarter");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Construct an intent that will execute the AlarmReceiver
        Intent intentMorningAlarmStarter = new Intent(getApplicationContext(), MorningReceiver.class);

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MorningReceiver.REQUEST_CODE,
                intentMorningAlarmStarter, PendingIntent.FLAG_UPDATE_CURRENT);


        SharedPreferences prefs = getDefaultSharedPreferences(this);

        minute = prefs.getInt("prefStartTime", 123) % 60;
        hour = prefs.getInt("prefStartTime", 123);
        hour = hour - minute;
        hour = hour / 60;




        Calendar dailyCalendar = Calendar.getInstance();
        dailyCalendar.setTimeInMillis(System.currentTimeMillis());
        dailyCalendar.set(Calendar.HOUR_OF_DAY, hour);
        dailyCalendar.set(Calendar.MINUTE, minute);
        dailyCalendar.set(Calendar.SECOND, 1);
        AlarmManager morningAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        morningAlarm.setRepeating(AlarmManager.RTC_WAKEUP, dailyCalendar.getTimeInMillis(),
                24*60*60*1000, pIntent);
        // This runs at the set time every morning (morningAlarm)
        Log.i("Morning Service:", " running");

        WakefulBroadcastReceiver.completeWakefulIntent(intent); //for the boot receiver, clears the wake lock
    }
}