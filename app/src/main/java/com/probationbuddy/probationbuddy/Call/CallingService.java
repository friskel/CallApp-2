package com.probationbuddy.probationbuddy.Call;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.probationbuddy.probationbuddy.DoYouTestActivity;
import com.probationbuddy.probationbuddy.R;

public class CallingService extends Service{

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("asdfasf", " service start");
        super.onCreate();
        MyPhoneListener phoneStateListener = new MyPhoneListener(getApplicationContext());
        TelephonyManager telephonymanager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telephonymanager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


        Intent notificationIntent = new Intent(this, DoYouTestActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_favorites)
                .setContentTitle("Probation Buddy")
                .setContentText("Calling now!")
                .setContentIntent(pendingIntent).build();

        startForeground(1234567, notification);


    }

    @Override
    public void onDestroy() {
        Log.i("asdfa", "service destroy");
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.cancel(1234567); //changed # from 9?


    }

}




