package com.probationbuddy.probationbuddy.GoTestAlarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.probationbuddy.probationbuddy.MainActivity;
import com.probationbuddy.probationbuddy.R;
import com.probationbuddy.probationbuddy.Services.HideNotificationService;
import com.probationbuddy.probationbuddy.TestDoneActivity;

public class GoTestAlarmService extends IntentService {
    int mId;


    public GoTestAlarmService() {
        super("DayAlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long[] vibratePattern = {1,200,500,1000};

        // notif repeated

        //for first completed action
        Intent intentGoTestDone = new Intent(this, TestDoneActivity.class);
        PendingIntent pIntentGoTestDone = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intentGoTestDone, 0);

        //for second hide action
        Intent intentHide = new Intent(this, HideNotificationService.class);
        PendingIntent pIntentHide = PendingIntent.getService(this, (int) System.currentTimeMillis(), intentHide, 0);



        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();

        //////////////////////////////////////////////////////////////////////  build notification
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_favorites)
                        .setContentTitle("YOU HAVE TO TEST TODAY")
                        .setContentText("Probation Buddy: You are required to test today!")
                        .setTicker("Probation test today, don't forget!")
                        .setAutoCancel(true)
                        .setPriority(2) //-2 to 2
                        .setVibrate(vibratePattern)
                        .addAction(R.drawable.ic_restaurants, "Completed", pIntentGoTestDone)
                        .addAction(R.drawable.ic_nearby, "Hide", pIntentHide);


// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(TestDoneActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.


        mNotificationManager.notify(mId, mBuilder.build());

        Log.i("DayAlarmService", "Service running");
        wl.release();

    }
}
