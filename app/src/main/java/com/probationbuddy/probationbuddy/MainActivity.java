package com.probationbuddy.probationbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import com.probationbuddy.probationbuddy.gotestalarm.GoTestAlarmReceiver;
import com.probationbuddy.probationbuddy.morningalarm.MorningReceiver;
import com.probationbuddy.probationbuddy.morningalarm.MorningServiceStarter;
import com.probationbuddy.probationbuddy.settings.SettingsFragment;

import static com.probationbuddy.probationbuddy.R.id.bug;

public class MainActivity extends AppCompatActivity {
    Context mContext = this;
    SharedPreferences sharedPrefs;
    String callNumber;
    AlarmManager alarm;
    boolean alarmIsActive;
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

            case bug:
                Intent intentBug = new Intent(Intent.ACTION_SENDTO);
                intentBug.setType("text/email");
                intentBug.setData(Uri.parse("mailto: studiofrisky@gmail.com")); // only email apps should handle this
                intentBug.putExtra(Intent.EXTRA_EMAIL, new String[] { "studiofrisky@gmail.com" });
                intentBug.putExtra(Intent.EXTRA_SUBJECT, "Report a Bug - Probation Buddy");
                intentBug.putExtra(Intent.EXTRA_TEXT, "I appreciate all bug reports and work on fixing them ASAP!  Thank you!" + "\n \nEnter bug here: \n \n");
                if (intentBug.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intentBug, "Choose your email app:"));
                }
                return true;

            case R.id.suggest:
                Intent intentSuggest = new Intent(Intent.ACTION_SENDTO);
                intentSuggest.setType("text/email");
                intentSuggest.setData(Uri.parse("mailto: studiofrisky@gmail.com")); // only email apps should handle this
                intentSuggest.putExtra(Intent.EXTRA_EMAIL, new String[] { "studiofrisky@gmail.com" });
                intentSuggest.putExtra(Intent.EXTRA_SUBJECT, "Suggest a Feature - Probation Buddy");
                intentSuggest.putExtra(Intent.EXTRA_TEXT, "Thank you for your suggestion!  I take these requests seriously and implement any idea which will help people.  Let me know if you want a reply and I will get back to you within 24 hours!" + "\n \nEnter suggestion here: \n \n");
                if (intentSuggest.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intentSuggest, "Choose your email app:"));
                }
                return true;



            case R.id.feedback:
                Intent intentFeedback = new Intent(Intent.ACTION_SENDTO);
                intentFeedback.setType("text/email");
                intentFeedback.setData(Uri.parse("mailto: studiofrisky@gmail.com")); // only email apps should handle this
                intentFeedback.putExtra(Intent.EXTRA_EMAIL, new String[] { "studiofrisky@gmail.com" });
                intentFeedback.putExtra(Intent.EXTRA_SUBJECT, "Feedback - Probation Buddy");
                intentFeedback.putExtra(Intent.EXTRA_TEXT, "Thank you for your feedback! (I read all of these emails)" + "\n \nEnter feedback here: \n \n");
                if (intentFeedback.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intentFeedback, "Choose your email app:"));
                }
                return true;

            case R.id.question:
                Intent intentQuestion = new Intent(Intent.ACTION_SENDTO);
                intentQuestion.setType("text/email");
                intentQuestion.setData(Uri.parse("mailto: studiofrisky@gmail.com")); // only email apps should handle this
                intentQuestion.putExtra(Intent.EXTRA_EMAIL, new String[] { "studiofrisky@gmail.com" });
                intentQuestion.putExtra(Intent.EXTRA_SUBJECT, "Question - Probation Buddy");
                intentQuestion.putExtra(Intent.EXTRA_TEXT, "I reply to all questions within 24 hours.  Thanks!" + "\n \n Enter question here: \n \n");
                if (intentQuestion.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intentQuestion, "Choose your email app:"));
                }
                return true;

            case R.id.rate:
                //////
                rateApp();
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
        Snackbar.make(findViewById(android.R.id.content), R.string.you_test_today_toast, Snackbar.LENGTH_INDEFINITE)
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
        Snackbar.make(findViewById(android.R.id.content), R.string.called_no_tests_toast, Snackbar.LENGTH_INDEFINITE)
                .setAction("Call Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callNumber = sharedPrefs.getString("prefsCallNumber", "not set!");
                        if (callNumber.equals("not set!") || callNumber.equals("")) {
                            new AlertDialog.Builder(mContext)
                                    .setTitle(R.string.hold_on)
                                    .setMessage(R.string.need_to_set_number)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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
        Snackbar.make(findViewById(android.R.id.content), R.string.not_called_today_toast, Snackbar.LENGTH_INDEFINITE)
                .setAction("Call Now", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callNumber = sharedPrefs.getString("prefsCallNumber", "not set!");
                        if (callNumber.equals("not set!") || callNumber.equals("")) {
                            new AlertDialog.Builder(mContext)
                                    .setTitle(R.string.hold_on)
                                    .setMessage(R.string.need_to_set_number)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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
                                    .setTitle(R.string.hold_on)
                                    .setMessage(R.string.need_to_set_number)
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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

            firstRunDialog();
        }
    }

    private void firstRunDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hello!")
                .setMessage(getString(R.string.first_run1) +
                        getString(R.string.first_run2) +
                        getString(R.string.first_run3))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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

    private void snackbarHelpGuide() {
        Snackbar.make(findViewById(android.R.id.content), R.string.need_help_getting_started, Snackbar.LENGTH_INDEFINITE)
                .setAction("Guide", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firstRunDialog();
                        }
                })
                .setActionTextColor(Color.RED)
                .show();
    }

//    public String getStartTime() {
//        SharedPreferences prefs = getDefaultSharedPreferences(this);
//
//        //set hour and minute of morning start time
//        minute = prefs.getInt("prefStartTime", 123) % 60;
//        hour = prefs.getInt("prefStartTime", 123);
//        hour = hour - minute;
//        hour = hour / 60;
//
//        am12 = false;
//
//        minuteString = String.valueOf(minute);
//        if (minute < 10) {
//            minuteString = "0" + minute;
//        }
//
//
//        if (hour == 0) {
//            hour = 12;
//            am12 = true;
//
//        }
//
//        time = (hour + ":" + minuteString + " am");
//
//        if (hour > 12) {
//            hour = hour - 12;
//            time = (hour + ":" + minuteString + " pm");
//        }
//        if (hour == 12) {
//            time = (hour + ":" + minuteString + " pm");
//            if (am12) {
//                time = (hour + ":" + minuteString + " am");
//            }
//        }
//        return time;
//    }


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


    /*
* Start with rating the app
* Determine if the Play Store is installed on the device
*
* */
    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }




}