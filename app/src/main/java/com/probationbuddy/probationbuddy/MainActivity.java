package com.probationbuddy.probationbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.probationbuddy.probationbuddy.Call.CallActivity2;
import com.probationbuddy.probationbuddy.DayAlarm.DayAlarmReceiver;
import com.probationbuddy.probationbuddy.GoTestAlarm.GoTestAlarmReceiver;
import com.probationbuddy.probationbuddy.Log.LogActivity;
import com.probationbuddy.probationbuddy.MorningAlarm.MorningReceiver;
import com.probationbuddy.probationbuddy.MorningAlarm.MorningServiceStarter;
import com.probationbuddy.probationbuddy.SettingsNew.SettingsFragment;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    int minute;
    int hour;
    boolean am12;
    boolean alarmIsActive;
    String time;
    String minuteString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //set activity layout UPDATE TEST

        //set toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

        //set fragment with all of the settings
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.settingsFragmentHome, new SettingsFragment()).commit();

//        //get sharedprefs object if needed
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //show dialog on first run
        checkFirstRun();

        openSnackbar();

    } //end of onCreate

    private void openSnackbar() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean calledToday = sharedPrefs.getBoolean("calledToday", false);


        if (calledToday) {
            Snackbar.make(findViewById(android.R.id.content), "You have called in today.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Call Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intentCall = new Intent(getApplicationContext(), CallActivity2.class);
                            intentCall.putExtra("callNow", true);
                            startActivity(intentCall);
                        }
                    })
                    .setActionTextColor(Color.GREEN)
                    .show();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "You have not called in today!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Call Now", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intentCall = new Intent(getApplicationContext(), CallActivity2.class);
                            intentCall.putExtra("callNow", true);
                            startActivity(intentCall);
                        }
                    })
                    .setActionTextColor(Color.RED)
                    .show();
        }

    }


    //activity state callbacks
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass

        saveAndStartAlarms();


        Log.i("onPause", "now");


    } //runs saveAndStartAlarms(); when you close activity

    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass

        Log.i("onDestroy", "now");


    }


    //for toolbar buttons
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


            case R.id.action_call:

                Intent intentCall = new Intent(this, CallActivity2.class);

                startActivity(intentCall);


                return true;
            case R.id.action_testdone:

                Intent intentTestDone = new Intent(this, TestDoneActivity.class);

                startActivity(intentTestDone);


                return true;
            case R.id.action_doyoutest:

                Intent intentDoYouTest = new Intent(this, DoYouTestActivity.class);

                startActivity(intentDoYouTest);


                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    } //sets the clicks for the toolbar menu items


    //when you click save button in toolbar
    public void saveAndStartAlarms() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);


        alarmIsActive = sharedPrefs.getBoolean("prefsActivate", true);

        Log.i("saveAndStart", "now");

        if (!alarmIsActive) {

            cancelMorningAlarm();
            stopDayAlarm();
            stopGoTestAlarm();

        } else {

            stopDayAlarm();
            stopGoTestAlarm();
            startService(new Intent(MainActivity.this, MorningServiceStarter.class));


        }


    }


    //alarm cancels
    public void cancelMorningAlarm() {
        Intent intent = new Intent(getApplicationContext(), MorningReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MorningReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager morningAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        morningAlarm.cancel(pIntent);
    }//cancels morningAlarm

    public void stopDayAlarm() {
        Intent intentDay = new Intent(getApplicationContext(), DayAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, DayAlarmReceiver.REQUEST_CODE,
                intentDay, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager dayAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        dayAlarm.cancel(pIntent);
    }//cancels dayAlarm

    public void stopGoTestAlarm() {
        Intent intentGo = new Intent(getApplicationContext(), GoTestAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, GoTestAlarmReceiver.REQUEST_CODE,
                intentGo, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager goAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        goAlarm.cancel(pIntent);
    }//cancels dayAlarm

    public void checkFirstRun() {
        SharedPreferences sharedPrefsFirstRun = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isFirstRun = sharedPrefsFirstRun.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            // Place your dialog code here to display the dialog
            new AlertDialog.Builder(this)
                    .setTitle("Welcome!")
                    .setMessage("To setup Probation Buddy, just configure your settings and hit the save button in the top toolbar.  For more help, click the 3 dots in the top right and select 'Help'.")
                    .setPositiveButton("OK", null)
                    .show();

            sharedPrefsFirstRun
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public String getStartTime() {
        SharedPreferences prefs = getDefaultSharedPreferences(this);

        //set hour and minute of morning start time
        minute = prefs.getInt("prefStartTime", 123) % 60;
        hour = prefs.getInt("prefStartTime", 123);
        hour = hour - minute;
        hour = hour / 60;

        am12 = false;

        minuteString = String.valueOf(minute);
        if (minute < 10) {
            minuteString = "0" + minute;
        }


        if (hour == 0) {
            hour = 12;
            am12 = true;

        }

        time = (hour + ":" + minuteString + " am");

        if (hour > 12) {
            hour = hour - 12;
            time = (hour + ":" + minuteString + " pm");
        }
        if (hour == 12) {
            time = (hour + ":" + minuteString + " pm");
            if (am12) {
                time = (hour + ":" + minuteString + " am");
            }
        }
        return time;
    }


}