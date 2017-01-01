package com.probationbuddy.probationbuddy.calendarlog;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.probationbuddy.probationbuddy.R;

import java.util.Calendar;

public class CalendarLogActivity extends AppCompatActivity {
    long currentTime;
    int logType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_log);
        currentTime = System.currentTimeMillis();

        logType = getIntent().getExtras().getInt("logType", 0);

        if (logType == 1){
            calendarIntentNoTest();
        }
        if (logType == 2){
            calendarIntentYesTest();
        }
        if (logType == 3){
            calendarIntentTestDone();
        }






    }

    private void calendarIntentTestDone() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTimeInMillis(currentTime);
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(currentTime + 5000);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Test complete 12/23/17.  Type:")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Probation Buddy test log.  Test was completed as of 5:13pm on 12/23/17")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Enter test location")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivityForResult(intent, 0);
    }

    private void calendarIntentYesTest() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTimeInMillis(currentTime);
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(currentTime + 5000);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Called 12/23/17, Test required today!")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Probation Buddy call log.  Call made at 1:47pm on 12/23/17, test required.  ")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
        Toast.makeText(getApplication(), "Your call has been logged!",
                Toast.LENGTH_LONG).show();
    }

    private void calendarIntentNoTest() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTimeInMillis(currentTime);
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(currentTime + 5000);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Called 12/23/17, no tests")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Probation Buddy call log.  Call made at 1:47pm on 12/23/17, no tests are required.  ")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivityForResult(intent, 0);

    }


}
