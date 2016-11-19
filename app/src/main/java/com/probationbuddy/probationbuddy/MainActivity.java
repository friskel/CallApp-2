package com.probationbuddy.probationbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.probationbuddy.probationbuddy.DayAlarm.DayAlarmReceiver;
import com.probationbuddy.probationbuddy.Log.LogActivity;
import com.probationbuddy.probationbuddy.MorningAlarm.MorningReceiver;
import com.probationbuddy.probationbuddy.MorningAlarm.MorningService;
import com.probationbuddy.probationbuddy.Settings.SettingsActivity;
import com.probationbuddy.probationbuddy.SettingsNew.SettingsFragment;

public class MainActivity extends AppCompatActivity {
    int morningHour = 0;
    int morningMinute = 18;
    String myNumber;
    String interval;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3); //set activity layout UPDATE TEST

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top); //set toolbar
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
        //set toolbar

//
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.settingsFragmentHome, new SettingsFragment()).commit();




        Log.i("test", "test");



        //shared preferences settings ---------
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt("morningHour", morningHour);
        editor.putInt("morningMinute", morningMinute);
        editor.putLong("dayAlarmInterval", AlarmManager.INTERVAL_FIFTEEN_MINUTES);
        editor.apply();
        //end shared preference settings ---------



//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        myNumber = sharedPrefs.getString("myCallNumber", "def");
//        TextView tv1 = (TextView)findViewById(R.id.textMainTest);
//        tv1.setText(myNumber);
//
//        interval = sharedPrefs.getString("intervalPref", "5");
//
//        TextView tv2 = (TextView)findViewById(R.id.tvInterval);
//        tv2.setText(interval);
//
//
//
//
//
//                //start morningAlarm +++
//        Button startMorningButton = (Button) findViewById(R.id.startMorningButton);
//        startMorningButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scheduleMorningAlarm();
//            }
//        });
//
//        //cancel morningAlarm ---
//        Button stopMorningButton = (Button) findViewById(R.id.stopMorningButton);
//        stopMorningButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancelMorningAlarm();
//            }
//        });
//
//
//
//        //start dayAlarm +++
//        Button stopDayButton = (Button) findViewById(R.id.stopDayButton);
//        stopDayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopDayAlarm();
//            }
//        });
//
//        //start callactivity
//        Button callButton = (Button) findViewById(R.id.callButton);
//        callButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentCall = new Intent(getApplicationContext(), com.probationbuddy.probationbuddy.Call.CallActivity.class);
//
//                startActivity(intentCall);
//            }
//        });









    } //end of onCreate



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    } //add menu items to toolbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log:
                Intent intentLog = new Intent(this, LogActivity.class);

                startActivity(intentLog);
                return true;

            case R.id.action_settings:

                Intent intentSettings = new Intent(this, SettingsActivity.class);

                startActivity(intentSettings);


                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    } //sets the clicks for the toolbar menu items





    public void scheduleMorningAlarm() {

        startService(new Intent(this, MorningService.class));


    } //starts MorningServiceStarter service which starts morningAlarm

    public void cancelMorningAlarm() {
        Intent intent = new Intent(getApplicationContext(), MorningReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MorningReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager morningAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        morningAlarm.cancel(pIntent);
    }//cancels morningAlarm



    public void stopDayAlarm() {
        Intent intent = new Intent(getApplicationContext(), DayAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, DayAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager dayAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        dayAlarm.cancel(pIntent);
    }//cancels dayAlarm


}