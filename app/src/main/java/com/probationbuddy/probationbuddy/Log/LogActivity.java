package com.probationbuddy.probationbuddy.Log;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.probationbuddy.probationbuddy.R;

public class LogActivity extends AppCompatActivity {
    TextView tv;
    int minute;
    int hour;
    int time;

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


        tv = (TextView)findViewById(R.id.tvTime);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        minute = sharedPrefs.getInt("prefStartTime", 123) % 60;
        hour = sharedPrefs.getInt("prefStartTime", 123);
        hour = hour - minute;
        hour = hour / 60;

        if (hour > 12) {
            hour = hour-12;
        }



        tv.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
    }
}
