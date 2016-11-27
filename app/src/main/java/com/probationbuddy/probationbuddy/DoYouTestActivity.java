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

import com.probationbuddy.probationbuddy.DayAlarm.DayAlarmReceiver;
import com.probationbuddy.probationbuddy.GoTestAlarm.GoTestAlarmStarter;

public class DoYouTestActivity extends AppCompatActivity {
    TextView colorTv;
    String yourColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_you_test);
        setTheme(R.style.AppTheme);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top); //set toolbar UPDATE TEST
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
        //set toolbar

        colorTv = (TextView) findViewById(R.id.colorTv);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        yourColor = sharedPrefs.getString("prefsColorNumber", "def");
        colorTv.setText(yourColor);


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
                                endDayAlarm();
                                Toast.makeText(getApplicationContext(), "Go test reminders are starting!",
                                        Toast.LENGTH_LONG).show();
                                startService(new Intent(getApplicationContext(), GoTestAlarmStarter.class));
                            }
                        })
                        .show();



            }
        });

        //no test; end dayAlarm
        Button noTestButton = (Button) findViewById(R.id.noTestButton);
        noTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancelAll();

                endDayAlarm();
                Toast.makeText(getApplicationContext(), "Reminders stopping until tomorrow! :)",
                        Toast.LENGTH_LONG).show();
            }
        });

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

    public void endDayAlarm() {

//        Intent intentMorning = new Intent(getApplicationContext(), MorningReceiver.class);
//        final PendingIntent pIntentMorning = PendingIntent.getBroadcast(this, MorningReceiver.REQUEST_CODE,
//                intentMorning, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager morningAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        morningAlarm.cancel(pIntentMorning);

        Intent intentDay = new Intent(getApplicationContext(), DayAlarmReceiver.class);
        final PendingIntent pIntentDay = PendingIntent.getBroadcast(this, DayAlarmReceiver.REQUEST_CODE,
                intentDay, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager dayAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        dayAlarm.cancel(pIntentDay);
    }//cancels dayAlarm
}
