package com.probationbuddy.probationbuddy.CalendarLog;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.probationbuddy.probationbuddy.MainActivity;
import com.probationbuddy.probationbuddy.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarLogActivity extends AppCompatActivity {
    long currentTime;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_log);
        currentTime = System.currentTimeMillis();

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss, dd-MM-yyyy ", Locale.getDefault());
        formattedDate = df.format(c.getTime());


        int logType = getIntent().getIntExtra("logType", 0);


        if (logType == 1){
            calendarIntentNoTest();
        }
        else if (logType == 2){
            calendarIntentYesTest();
        }
        else if (logType == 3){
            calendarIntentTestDone();
        }
        else {
            calendarIntentNoTest();
        }


    }

    @Override
    protected void onStop()
    {
        super.onStop();
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
                .putExtra(CalendarContract.Events.TITLE, "Test complete " + formattedDate + ".  Type:")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Probation Buddy test log.  Test was completed as of " + formattedDate)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Enter test location")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivityForResult(intent, 0);
        finish();
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
                .putExtra(CalendarContract.Events.TITLE, "Called " + formattedDate +", Test required today!")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Probation Buddy call log.  Call made at " + formattedDate + ", test is required.")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivityForResult(intent, 0);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
        Toast.makeText(getApplication(), "Your call has been logged!",
                Toast.LENGTH_LONG).show();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(mainIntent);
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
                .putExtra(CalendarContract.Events.TITLE, "Called " + formattedDate + ", no tests")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Probation Buddy call log.  Call made at " + formattedDate + ", no tests are required.")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
        startActivityForResult(intent, 0);
        finish();
    }


}
