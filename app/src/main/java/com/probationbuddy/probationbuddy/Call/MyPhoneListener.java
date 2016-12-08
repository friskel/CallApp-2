package com.probationbuddy.probationbuddy.Call;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.probationbuddy.probationbuddy.DoYouTestActivity;

import static android.content.Context.TELEPHONY_SERVICE;

public class MyPhoneListener extends PhoneStateListener {

    private Context mContext = null;

    private static Boolean onCall = false;

    public MyPhoneListener(Context context){
        mContext = context;
    }

    public void onCallStateChanged(int state, String incomingNumber) {

        switch (state) {

            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.i("DEBUG", "OFFHOOK");

                onCall = true;

                break;

            case TelephonyManager.CALL_STATE_IDLE:
                Log.i("DEBUG", "IDLE");
                if (onCall){
                    //stuff
                    callFinished();
                    cancelListener();

                    Intent stopIntent = new Intent(mContext, CallingService.class);
                    mContext.stopService(stopIntent);

                    onCall = false;

                }

                break;
        }
    }

    private void callFinished() {


        //try way in stackoverflow answer?

        Intent restart = mContext.getPackageManager().
                getLaunchIntentForPackage(mContext.getPackageName());
        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(restart);

        Intent intent = new Intent(mContext, DoYouTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);

        Toast.makeText(mContext, "callFinished method running",
                Toast.LENGTH_SHORT).show();

    }


    private void cancelListener() {

        // cancel PhoneStateListener for monitoring
        MyPhoneListener phoneListener = new MyPhoneListener(mContext);
        TelephonyManager telephonyManager =
                (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener, MyPhoneListener.LISTEN_NONE);
        Log.i("call -", "cancelListener");
    }



}