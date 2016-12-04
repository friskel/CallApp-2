package com.probationbuddy.probationbuddy.Log;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.probationbuddy.probationbuddy.DayAlarm.DayAlarmReceiver;
import com.probationbuddy.probationbuddy.R;

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






    }
}
