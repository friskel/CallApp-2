package com.probationbuddy.probationbuddy;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.probationbuddy.probationbuddy.call.CallActivity2;
import com.probationbuddy.probationbuddy.dayalarm.DayAlarmReceiver;
import com.probationbuddy.probationbuddy.gotestalarm.GoTestAlarmStarter;
import com.probationbuddy.probationbuddy.services.HideNotificationService;

public class DoYouTestActivity extends AppCompatActivity {
    Context mContext = this;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_you_test);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setTheme(R.style.AppTheme);
        setToolbar();
        cancelAllNotifications();
        setColorTextView();

        makeYesTestButton();
        makeNoTestButton();
        makeCallLaterButton();
        makeCallNowButton();

    }

    private void makeYesTestButton() {
        //yes you have to test; start GoTestAlarmService - button
        Button goTestButton = (Button) findViewById(R.id.testTodaybutton);
        goTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new android.support.v7.app.AlertDialog.Builder(DoYouTestActivity.this)
                        .setTitle("Test required today")
                        .setMessage("Press OK to confirm that you have to go test today.  This will activate reminders to make sure you don't forget to go!")


                        .setNeutralButton("Cancel", null)

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //to method?
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                editor.putBoolean("calledToday", true);
                                editor.putBoolean("haveTestToday", true);
                                editor.apply();

                                endDayAlarm();
                                dayAlarmToFalse();
                                Toast.makeText(mContext, "Go test reminders are starting!",
                                        Toast.LENGTH_LONG).show();
                                startService(new Intent(mContext, GoTestAlarmStarter.class));
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

                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("calledToday", true);
                editor.apply();


                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancelAll();

                dayAlarmToFalse();
                endDayAlarm();
                Toast.makeText(mContext, "Reminders stopping until tomorrow! :)",
                        Toast.LENGTH_LONG).show();


            }
        });
    }

    private void makeCallLaterButton() {
        //call again later button
        Button callLaterButton = (Button) findViewById(R.id.callLaterButton);
        callLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(DoYouTestActivity.this)
                        .setTitle("Call back required")
                        .setMessage("Alarms will stay on")
                        .setPositiveButton("OK", null)
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

        if (dayAlarm!= null) {
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
}
