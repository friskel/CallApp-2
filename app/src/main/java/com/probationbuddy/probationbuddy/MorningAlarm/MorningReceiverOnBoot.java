package com.probationbuddy.probationbuddy.MorningAlarm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

// WakefulBroadcastReceiver ensures the device does not go back to sleep
// during the startup of the service
public class MorningReceiverOnBoot extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Launch the specified service when this message is received
        Intent startServiceIntent = new Intent(context, MorningServiceStarter.class);
        startWakefulService(context, startServiceIntent);
    }
}


// on boot start MorningServiceStarter service to reset MorningAlarm