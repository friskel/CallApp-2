package com.probationbuddy.probationbuddy.MorningAlarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.probationbuddy.probationbuddy.DayAlarm.DayAlarmReceiver;


// MorningService makes the alarm to give repeating notification reminders
// It starts every morning once when MorningReceiver is fired


public class MorningService extends IntentService {

    String intervalPrefString;
    int intervalPref;
    long interval;
    long firstMillis;

    public MorningService() {
        super("MorningService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        //get shared prefs
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);


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



        // This alarm runs at the set time every morning, to start this daily repeating reminder alarm

        //starts right away and repeats every (interval) minutes

        // Make the intent to fire DayAlarmReceiver
        Intent intentDayAlarmStart = new Intent(getApplicationContext(), DayAlarmReceiver.class);

        // Make PendingIntent to be triggered each time the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, DayAlarmReceiver.REQUEST_CODE,
                intentDayAlarmStart, PendingIntent.FLAG_UPDATE_CURRENT);

        // alarm is set right away
        firstMillis = System.currentTimeMillis();

        //make the alarm
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, interval, pIntent);

    }
}