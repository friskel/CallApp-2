package com.probationbuddy.probationbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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

import com.probationbuddy.probationbuddy.call.CallActivity2;
import com.probationbuddy.probationbuddy.dayalarm.DayAlarmReceiver;
import com.probationbuddy.probationbuddy.feedback.FeedbackActivity;
import com.probationbuddy.probationbuddy.gotestalarm.GoTestAlarmReceiver;
import com.probationbuddy.probationbuddy.morningalarm.MorningReceiver;
import com.probationbuddy.probationbuddy.morningalarm.MorningServiceStarter;
import com.probationbuddy.probationbuddy.settings.SettingsFragment;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {
    Context mContext = this;
    SharedPreferences sharedPrefs;
    String callNumber;
    AlarmManager alarm;

    int minute;
    int hour;
    boolean am12;
    boolean alarmIsActive;
    String time;
    String minuteString;

    boolean calledToday;
    boolean haveTestToday;
    boolean alarmsActive;

    boolean isFirstRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        setMainSettingsFragment();

        isFirstRun = sharedPrefs.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            checkFirstRun(); //show welcome dialog on first run
        }

//        openSnackbar();

    }

    // start alarm stuff
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass

        saveAndStartAlarms();


        Log.i("onPause", "now");


    } //runs saveAndStartAlarms(); when you close activity


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass

        openSnackbar();

    } //runs saveAndStartAlarms(); when you close activity


    public void saveAndStartAlarms() {

        alarmIsActive = sharedPrefs.getBoolean("prefsActivate", true);

        Log.i("saveAndStart", "now");

        if (!alarmIsActive) {

            cancelMorningAlarm();
            stopDayAlarm();
            stopGoTestAlarm();

        } else {

            //not sure why i had it stop these...
//            stopDayAlarm();
//            stopGoTestAlarm();
            startService(new Intent(MainActivity.this, MorningServiceStarter.class));


        }


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




            case R.id.action_call:

                Intent intentCall = new Intent(this, CallActivity2.class);

                startActivity(intentCall);


                return true;

            case R.id.feedback:

                Intent intentFeedback = new Intent(this, FeedbackActivity.class);

                startActivity(intentFeedback);


                return true;
            


            case R.id.action_help:

                Intent intentHelp = new Intent(this, HelpGuideActivity.class);

                startActivity(intentHelp);


                return true;

            case R.id.activity_about:

                Intent intentAbout = new Intent(this, AboutActivity.class);

                startActivity(intentAbout);


                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    } //sets the clicks for the toolbar menu items


    // snackbar stuff
    public void openSnackbar() {
//        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        isFirstRun = sharedPrefs.getBoolean("isFirstRun", true);
        calledToday = sharedPrefs.getBoolean("calledToday", false);
        haveTestToday = sharedPrefs.getBoolean("haveTestToday", false);
        alarmsActive = sharedPrefs.getBoolean("prefsActivate", false);

        if (calledToday && haveTestToday){
            snackbarYesTest();
            return;
        }
        if (calledToday && alarmsActive){
            snackbarNoTest();
            return;
        }
        if (!alarmsActive){
            snackbarAlarmsOff();
            return;
        }
        snackbarNotCalledToday();

    }

    private void snackbarYesTest() {
        Snackbar.make(findViewById(android.R.id.content), "You have to test today!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Done", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentDone = new Intent(mContext, TestDoneActivity.class);
                        startActivity(intentDone);
                    }

                }) //done opens DoYouTest
                .setActionTextColor(Color.RED)
                .show();
    }

    private void snackbarNoTest() {
        Snackbar.make(findViewById(android.R.id.content), "You have called in today, no tests.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Call Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callNumber = sharedPrefs.getString("prefsCallNumber", "not set!");
                        if (callNumber.equals("not set!") || callNumber.equals("")) {
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Hold on..")
                                    .setMessage("You need to set your call-in phone number before making a call!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intentRestartMain = new Intent(getApplicationContext(), MainActivity.class);

                                            startActivity(intentRestartMain);
                                        }
                                    })
                                    .show();
                        } else {
                            Intent intentCall = new Intent(mContext, CallActivity2.class);
                            intentCall.putExtra("callNow", true);
                            startActivity(intentCall);
                        }
                    }
                })
                .setActionTextColor(Color.GREEN)
                .show();
    }

    private void snackbarNotCalledToday() {
        Snackbar.make(findViewById(android.R.id.content), "You have not called in today!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Call Now", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callNumber = sharedPrefs.getString("prefsCallNumber", "not set!");
                        if (callNumber.equals("not set!") || callNumber.equals("")) {
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Hold on..")
                                    .setMessage("You need to set your call-in number first before making a call!  ")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intentRestartMain = new Intent(mContext, MainActivity.class);
                                            startActivity(intentRestartMain);
                                        }
                                    })
                                    .show();
                        } else {
                            Intent intentCall = new Intent(mContext, CallActivity2.class);
                            intentCall.putExtra("callNow", true);
                            startActivity(intentCall);
                        }
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }

    private void snackbarAlarmsOff() {
        Snackbar.make(findViewById(android.R.id.content), "Reminders are disabled.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Call Now", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callNumber = sharedPrefs.getString("prefsCallNumber", "not set!");
                        if (callNumber.equals("not set!") || callNumber.equals("")) {
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Hold on..")
                                    .setMessage("You need to set your call-in number first before making a call!  ")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intentRestartMain = new Intent(mContext, MainActivity.class);
                                            startActivity(intentRestartMain);
                                        }
                                    })
                                    .show();
                        } else {
                            Intent intentCall = new Intent(mContext, CallActivity2.class);
                            intentCall.putExtra("callNow", true);
                            startActivity(intentCall);
                        }
                    }

                })
                .setActionTextColor(Color.RED)
                .show();
    }


    //alarm cancels
    private void cancelMorningAlarm() {
        Intent intent = new Intent(getApplicationContext(), MorningReceiver.class);
        final PendingIntent pIntentCancelMorning = PendingIntent.getBroadcast(this, MorningReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(pIntentCancelMorning);
    }//cancels morningAlarm

    private void stopDayAlarm() {
        Intent intentDay = new Intent(getApplicationContext(), DayAlarmReceiver.class);
        final PendingIntent pIntentStopDay = PendingIntent.getBroadcast(this, DayAlarmReceiver.REQUEST_CODE,
                intentDay, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(pIntentStopDay);
    }//cancels dayAlarm

    private void stopGoTestAlarm() {
        Intent intentGo = new Intent(getApplicationContext(), GoTestAlarmReceiver.class);
        final PendingIntent pIntentStopGoTest = PendingIntent.getBroadcast(this, GoTestAlarmReceiver.REQUEST_CODE,
                intentGo, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(pIntentStopGoTest);
    }//cancels dayAlarm


    private void checkFirstRun() {

        if (isFirstRun) {

            new AlertDialog.Builder(this)
                    .setTitle("Hello!")
                    .setMessage("To activate the daily repeating reminders, turn on the toggle switch at the top of the settings list.  Then set your call-in phone number and start time, and you are good to go!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            snackbarHelpGuide();
                        }
                    })
                    .setCancelable(false)
                    .show();

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }
    }

    private void snackbarHelpGuide() {
        Snackbar.make(findViewById(android.R.id.content), "Need help getting started?", Snackbar.LENGTH_INDEFINITE)
                .setAction("Help Guide", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentHelp = new Intent(mContext, HelpGuideActivity.class);
                        startActivity(intentHelp);
                        }
                })
                .setActionTextColor(Color.RED)
                .show();
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


    private void setMainSettingsFragment() {
        //set fragment with all of the settings
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.settingsFragmentHome, new SettingsFragment()).commit();
    }

    private void setToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
    }




}