package com.probationbuddy.probationbuddy.Log;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.probationbuddy.probationbuddy.DayAlarm.DayAlarmReceiver;
import com.probationbuddy.probationbuddy.R;

import java.util.Calendar;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

public class LogActivity extends AppCompatActivity {
    Button startMorningButton;
    Button getTimes;
    TextView systemTime;
    TextView setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top); //set toolbar UPDATE TEST
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
        //set toolbar

        startMorningButton = (Button)findViewById(R.id.startMorningButton);
        startMorningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDayAlarmStart = new Intent(getApplicationContext(), DayAlarmReceiver.class);

                // Make PendingIntent to be triggered each time the alarm goes off
                final PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), DayAlarmReceiver.REQUEST_CODE,
                        intentDayAlarmStart, PendingIntent.FLAG_UPDATE_CURRENT);

                try {
                    pIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        });


        systemTime = (TextView)findViewById(R.id.tvSystemTime);
        setTime = (TextView)findViewById(R.id.tvSetTime);

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(System.currentTimeMillis());





        getTimes = (Button)findViewById(R.id.button2);
        getTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar rightNow = Calendar.getInstance();
                rightNow.setTimeInMillis(System.currentTimeMillis());

                long nowHours = rightNow.get(Calendar.HOUR_OF_DAY);
                long nowHoursMillis = nowHours * 3600000;

                long nowMinutes = rightNow.get(Calendar.MINUTE);
                long nowMinutesMillis = nowMinutes * 60000;

                long nowSeconds = rightNow.get(Calendar.SECOND);
                long nowSecondsMillis = nowSeconds * 1000;

                long nowMillis = rightNow.get(Calendar.MILLISECOND);

                long nowFullTime = nowHoursMillis + nowMinutesMillis + nowSecondsMillis + nowMillis;



                SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
                long fullTimeNumber = prefs.getInt("prefStartTime", 123);

               long fullTimeNumberMillis = fullTimeNumber * 60000;


                systemTime.setText(String.valueOf(nowFullTime));
                setTime.setText(String.valueOf(fullTimeNumberMillis));
            }
        });



    }
}
