package com.probationbuddy.probationbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.probationbuddy.probationbuddy.DayAlarm.DayAlarmReceiver;
import com.probationbuddy.probationbuddy.Log.LogActivity;
import com.probationbuddy.probationbuddy.MorningAlarm.MorningReceiver;
import com.probationbuddy.probationbuddy.MorningAlarm.MorningServiceStarter;
import com.probationbuddy.probationbuddy.SettingsNew.SettingsFragment;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    int minute;
    int hour;
    String time;
    String minuteString;
    boolean am12;

    boolean alarmIsActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //set activity layout UPDATE TEST

        //set toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null) { getSupportActionBar().setLogo(R.mipmap.ic_launcher); getSupportActionBar().setDisplayUseLogoEnabled(true); }

        //set fragment with settings
        FragmentManager manager = getSupportFragmentManager(); FragmentTransaction transaction = manager.beginTransaction(); transaction.replace(R.id.settingsFragmentHome, new SettingsFragment()).commit();

        //get sharedprefs object
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //show dialog on first run
        checkFirstRun();

    } //end of onCreate

    //activity state callbacks
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass


        Log.i("onPause", "now");


    }
    @Override
    public void onStop() {
        super.onStop();  // Always call the superclass


        Log.i("onStop", "now");


    }
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
            case R.id.action_save:

                saveAndStartAlarms();



                return true;

            case R.id.action_log:
                Intent intentLog = new Intent(this, LogActivity.class);

                startActivity(intentLog);
                return true;

            case R.id.action_settings:

                Intent intentSettings = new Intent(this, TestDoneActivity.class);

                startActivity(intentSettings);


                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    } //sets the clicks for the toolbar menu items


    //when you click save button in toolbar
    public void saveAndStartAlarms(){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);


        alarmIsActive = sharedPrefs.getBoolean("prefsActivate", true);

        Log.i("saveAndStart", "now");

        if (!alarmIsActive){

            new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("Deactivate")
                    .setMessage(
                            "The 'activate reminders' toggle is switched off! \n \n" +
                            "Press OK to turn off all reminders and alarms.")


                    .setNegativeButton("Cancel", null)

                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            cancelMorningAlarm();
                            stopDayAlarm();
                            //add stop gototest alarm
                            Toast.makeText(getApplicationContext(), "Probation Buddy reminders have been turned OFF",
                                    Toast.LENGTH_LONG).show();

                        }
                    })
                    .show();




        }else{



            new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("Activate")
                    .setMessage(
                            "You are about to start Probation Buddy daily reminders! \n \n" +
                            "Start time: " + getStartTime() + " every day (beginning tomorrow) \n \n" +
                            "Press OK to confirm.")


                    .setNegativeButton("Cancel", null)

                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            stopDayAlarm();
                            startService(new Intent(MainActivity.this, MorningServiceStarter.class));

                            Toast.makeText(getApplicationContext(), "Probation Buddy is running!",
                                    Toast.LENGTH_LONG).show();

                        }
                    })
                    .show();



        }


    }



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


    // need to add stop goTestAlarm ?




    public void checkFirstRun() {
        SharedPreferences sharedPrefsFirstRun = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isFirstRun = sharedPrefsFirstRun.getBoolean("isFirstRun", true);
        if (isFirstRun){
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
        if (minute < 10){
            minuteString = "0" + minute;
        }


        if (hour == 0){
            hour = 12;
            am12 = true;

        }

        time = (hour + ":" + minuteString + " am");

        if (hour > 12){
            hour = hour-12;
            time = (hour + ":" + minuteString + " pm");
        }
        if (hour == 12){
            time = (hour + ":" + minuteString + " pm");
            if (am12){
                time = (hour + ":" + minuteString + " am");
            }
        }
        return time;
    }


}