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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_log);
        currentTime = System.currentTimeMillis();

        calendarIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
        Toast.makeText(getApplication(), "Your call has been logged!",
                Toast.LENGTH_LONG).show();
    }

    private void calendarIntent() {
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
