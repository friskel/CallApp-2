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
import android.widget.Toast;

import com.probationbuddy.probationbuddy.GoTestAlarm.GoTestAlarmReceiver;

public class TestDoneActivity extends AppCompatActivity {
    Button testDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_done);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top); //set toolbar UPDATE TEST
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
        //set toolbar

        //cancel GoTestAlarm ---
        testDoneButton = (Button) findViewById(R.id.testFinished);
        testDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelGoTestAlarm();
                goTestAlarmToFalse();

                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Confirm")
                        .setMessage("Press OK to confirm that you have finished your probation test.  Reminders for today will stop!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //put in method?
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.cancelAll();

                                Toast.makeText(getApplicationContext(), "Test complete, stopping alarms until tomorrow morning!",
                                        Toast.LENGTH_LONG).show();

                                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                editor.putBoolean("haveTestToday", false);
                                editor.apply();
                            }
                        })
                        .show();



            }
        });

        //test not finished
        testDoneButton = (Button) findViewById(R.id.testNotFinished);
        testDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Reminders are still active.  Go test!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cancelGoTestAlarm(){

        Intent intent = new Intent(getApplicationContext(), GoTestAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, GoTestAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager goTestAlarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        goTestAlarm.cancel(pIntent);
    }

    private void goTestAlarmToFalse() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("goTestAlarmRunning", false);
        editor.apply();
    }
}
