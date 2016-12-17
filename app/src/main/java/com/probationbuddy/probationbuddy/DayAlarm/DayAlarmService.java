package com.probationbuddy.probationbuddy.dayalarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.probationbuddy.probationbuddy.call.CallActivity2;
import com.probationbuddy.probationbuddy.DoYouTestActivity;
import com.probationbuddy.probationbuddy.MainActivity;
import com.probationbuddy.probationbuddy.R;
import com.probationbuddy.probationbuddy.services.HideNotificationService;

public class DayAlarmService extends IntentService {
    int mId = 100;


    public DayAlarmService() {
        super("DayAlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long[] vibratePattern = {1,200,500,400,200,200};

        // notif repeated

        //for first call action
        Intent intentCall = new Intent(this, CallActivity2.class);
        intentCall.putExtra("callNow", true);
        PendingIntent pIntentCall = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intentCall, 0);

        //for second hide action
        Intent intentHide = new Intent(this, HideNotificationService.class);
        PendingIntent pIntentHide = PendingIntent.getService(this, (int) System.currentTimeMillis(), intentHide, 0);

        //for third done action
        Intent intentDone = new Intent(this, DoYouTestActivity.class);
        PendingIntent pIntentDone = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intentDone, 0);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean prefsVibrate = sharedPrefs.getBoolean("prefsVibrate", true);
        boolean prefsSound = sharedPrefs.getBoolean("prefsSound", false);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();

        //////////////////////////////////////////////////////////////////////  build notification
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_contact_phone_black_48dp)
                        .setContentTitle("Probation Buddy")
                        .setContentText("You have not called today!  Click to call now.")
                        .setTicker("Probation Buddy:  Call now!")
                        .setAutoCancel(true)

                        .setPriority(2) //-2 to 2


                        .setColor(getResources().getColor(R.color.colorPrimaryDark))
                        .addAction(R.drawable.ic_perm_phone_msg_black_24dp, "Call Now", pIntentCall)
//                        .addAction(0, "Hide", pIntentHide)
                        .addAction(R.drawable.ic_power_settings_new_black_24dp, "Options", pIntentDone);

        if (prefsVibrate) {
            mBuilder.setVibrate(vibratePattern);
        }
        if (prefsSound) {
            mBuilder.setSound(soundUri);
        }


// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, CallActivity2.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
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

        WakefulBroadcastReceiver.completeWakefulIntent(intent);

    }
}