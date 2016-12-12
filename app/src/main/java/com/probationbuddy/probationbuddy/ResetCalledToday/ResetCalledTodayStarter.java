package com.probationbuddy.probationbuddy.ResetCalledToday;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.probationbuddy.probationbuddy.GoTestAlarm.GoTestAlarmReceiver;

import java.util.Calendar;


public class ResetCalledTodayStarter extends IntentService {

    public ResetCalledTodayStarter() {
        super("ResetCalledTodayStarter");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        // Construct an intent that will execute the AlarmReceiver
        Intent intentResetCalledToday = new Intent(getApplicationContext(), GoTestAlarmReceiver.class);

        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, GoTestAlarmReceiver.REQUEST_CODE,
                intentResetCalledToday, PendingIntent.FLAG_UPDATE_CURRENT);


        Calendar dailyCalendar = Calendar.getInstance();
        dailyCalendar.setTimeInMillis(System.currentTimeMillis());

        //start tomorrow morning 2am
        dailyCalendar.set(Calendar.HOUR_OF_DAY, 2);
        dailyCalendar.set(Calendar.MINUTE, 0);
        dailyCalendar.set(Calendar.SECOND, 0);
        dailyCalendar.add(Calendar.DATE, 1);

        //make alarm
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        //start it
        alarm.set(AlarmManager.RTC_WAKEUP, dailyCalendar.getTimeInMillis(), pIntent);

    }

}
