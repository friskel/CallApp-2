package com.probationbuddy.probationbuddy;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.probationbuddy.probationbuddy.calendarlog.CalendarLogActivity;
import com.probationbuddy.probationbuddy.call.CallActivity2;
import com.probationbuddy.probationbuddy.call.CallingService;
import com.probationbuddy.probationbuddy.dayalarm.DayAlarmReceiver;
import com.probationbuddy.probationbuddy.gotestalarm.GoTestAlarmStarter;
import com.probationbuddy.probationbuddy.services.HideNotificationService;

//wow this class needs to be re-done to remove the repetitiveness

public class DoYouTestActivity extends AppCompatActivity {
    Context mContext = this; //had to get context this way to make the toasts work ¯\_(ツ)_/¯
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_you_test);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(R.style.AppTheme);
        setToolbar();
        cancelAllNotifications();
        cancelForegroundNotification();
        setColorTextView();

        makeYesTestButton();
        makeNoTestButton();
        makeCallLaterButton();
        makeCallNowButton();



    }

    private void cancelForegroundNotification() {
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.cancel(1234567);

        stopService(new Intent(this, CallingService.class));

        //changed # from 9?
    }

    private void makeYesTestButton() {
        //yes you have to test; start GoTestAlarmService - button
        Button goTestButton = (Button) findViewById(R.id.testTodaybutton);
        goTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(DoYouTestActivity.this)
                        .setTitle(R.string.test_required_today)
                        .setMessage(R.string.ok_to_confirm_you_have_test_today)


                        .setNeutralButton(R.string.cancel, null)

                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //to method?
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                editor.putBoolean("calledToday", true);
                                editor.putBoolean("haveTestToday", true);
                                editor.apply();

                                endDayAlarm();
                                dayAlarmToFalse();
                                Toast.makeText(mContext, R.string.go_test_reminders_starting,
                                        Toast.LENGTH_LONG).show();
                                startService(new Intent(mContext, GoTestAlarmStarter.class));
                                if (sharedPrefs.getBoolean("prefsLogOn", false)) {
                                    makeLogDialog(2);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void makeNoTestButton() {
        //no test; end dayAlarm
        Button noTestButton = (Button) findViewById(R.id.noTestButton);
        noTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(DoYouTestActivity.this)
                        .setTitle(R.string.no_tests_today)
                        .setMessage(R.string.ok_confirm_no_tests_today)


                        .setNeutralButton(R.string.cancel, null)

                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //to method?
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                editor.putBoolean("calledToday", true);
                                editor.apply();

                                int callCount = sharedPrefs.getInt("callCount", 0);
                                callCount++;
                                editor.putInt("callCount", callCount);
                                editor.apply();



                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.cancelAll();

                                dayAlarmToFalse();
                                endDayAlarm();
                                Toast.makeText(mContext, R.string.reminders_stopping_until_tomorrow,
                                        Toast.LENGTH_LONG).show();

                                if (sharedPrefs.getBoolean("prefsLogOn", false)) {
                                    makeLogDialog(1);
                                }




                                if (callCount == 7 || callCount == 12 || callCount == 25 || callCount == 45) {
                                    askForRate();
                                }

                            }
                        })
                        .show();


            }
        });
    }

    private void askForRate() {
        new AlertDialog.Builder(DoYouTestActivity.this)
                .setTitle("Always ad-free!")
                .setMessage("If this app has helped you out, please give it a good rating on the app store!  If you think it sucks, give it one star!  Either way, I would really appreciate the rating - hit OK to go there now.")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rateApp();
                    }
                })
                .setNeutralButton("no", null)
                .show();

    }

    private void makeLogDialog(int logTypeInt) {

        final int logType = logTypeInt;

        new AlertDialog.Builder(DoYouTestActivity.this)
                .setTitle(R.string.log_call_question)
                .setMessage(R.string.ok_to_make_calendar_entry)


                .setNeutralButton(R.string.no, null)

                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent logIntent = new Intent(mContext, CalendarLogActivity.class);
                        logIntent.putExtra("logType", logType);
                        startActivity(logIntent);
                        finish();
                    }
                })
                .show();


    }

    private void makeCallLaterButton() {
        //call again later button
        Button callLaterButton = (Button) findViewById(R.id.callLaterButton);
        callLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(DoYouTestActivity.this)
                        .setTitle(R.string.call_back_required)
                        .setMessage(R.string.alarms_will_stay_on)
                        .setPositiveButton(R.string.ok, null)
                        .show();

            }
        });
    }

    private void makeCallNowButton() {
        //call now button
        Button callNowButton = (Button) findViewById(R.id.buttonCallNow);
        callNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCall = new Intent(mContext, CallActivity2.class);
                intentCall.putExtra("callNow", true);
                startActivity(intentCall);

            }
        });
    }


    private void dayAlarmToFalse() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("dayAlarmRunning", false);
        editor.apply();
    }  //dayAlarmRunning sharedPref to false

    public void endDayAlarm() {

        Intent intentDay = new Intent(mContext, DayAlarmReceiver.class);
        final PendingIntent pIntentDay = PendingIntent.getBroadcast(this, DayAlarmReceiver.REQUEST_CODE,
                intentDay, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager dayAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        if (dayAlarm != null) {
            dayAlarm.cancel(pIntentDay);
        }

    }//cancels dayAlarm


    private void setToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
    } //makes Toolbar

    private void setColorTextView() {
        TextView colorTv = (TextView) findViewById(R.id.colorTv);
        String yourColor = sharedPrefs.getString("prefsColorNumber", "def");
        colorTv.setText(yourColor);
    } //init TextView, get sharedpref, set TV

    private void cancelAllNotifications() {
        Intent startServiceIntent = new Intent(mContext, HideNotificationService.class);
        startService(startServiceIntent);
    }  //runs hide notification service


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
