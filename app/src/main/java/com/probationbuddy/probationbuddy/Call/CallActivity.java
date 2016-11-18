package com.probationbuddy.probationbuddy.Call;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.probationbuddy.probationbuddy.R;

public class CallActivity extends AppCompatActivity {
    String myNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        //////// set toolbar ///////
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
        /////// set toolbar ///////

        //cancel current notifications up top
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        myNumber = prefs.getString("myCallNumber", "123");

//        call right when activity opens
//        Intent intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel: +" + myNumber));
//        startActivity(intent);

        TextView myNumberTv = (TextView)findViewById(R.id.myNumberTv);
        myNumberTv.setText(myNumber);

        Button callNow = (Button) findViewById(R.id.buttonGoLog);



        // add PhoneStateListener for monitoring
        MyPhoneListener phoneListener = new MyPhoneListener();
        TelephonyManager telephonyManager =
                (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);


        callNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    addPhoneStateListener();
                    String uri = "tel:" + myNumber;
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));

                    startActivity(dialIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Your call has failed...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


    }

    public void addPhoneStateListener(){
        MyPhoneListener phoneListener = new MyPhoneListener();
        TelephonyManager telephonyManager =
                (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

    }


    public class MyPhoneListener extends PhoneStateListener {

        private boolean onCall = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String myColor = prefs.getString("myColorOrNumber", "123");

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // phone ringing...
                    Toast.makeText(CallActivity.this, incomingNumber + " calls you",
                            Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // one call exists that is dialing, active, or on hold
                    Toast.makeText(CallActivity.this, "Your test color/number: " + myColor,
                            Toast.LENGTH_LONG).show();
                    //because user answers the incoming call
                    onCall = true;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    // in initialization of the class and at the end of phone call

                    // detect flag from CALL_STATE_OFFHOOK
                    if (onCall == true) {
                        Intent restart = getBaseContext().getPackageManager().
                                getLaunchIntentForPackage(getBaseContext().getPackageName());
                        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(restart);

                        Intent intentCalled = new Intent(getApplicationContext(), com.probationbuddy.probationbuddy.DoYouTestActivity.class);

//
//                        intentCalled.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentCalled);

                        cancelListener();
                        onCall = false;
                        break;
                    }
                    break;
                default:
                    break;
            }




        }



        private void cancelListener(){

            // cancel PhoneStateListener for monitoring
//                        com.probationbuddy.probationbuddy.Call.CallActivity.MyPhoneListener phoneListener = new com.probationbuddy.probationbuddy.Call.CallActivity.MyPhoneListener();
            TelephonyManager telephonyManager =
                    (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
            // receive notifications of telephony state changes
            telephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
        }
    }
}






