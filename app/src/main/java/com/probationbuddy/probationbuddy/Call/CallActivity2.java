package com.probationbuddy.probationbuddy.call;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.probationbuddy.probationbuddy.R;

public class CallActivity2 extends AppCompatActivity {
    String myNumber;
    final static int MY_PERMISSIONS_CALL_PHONE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        try {
            callNow();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Your call has failed...",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            FirebaseCrash.report(new Exception("call failed, catch"));
        }




        //////// set toolbar ///////
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

        cancelCurrentNotifications();
        setNumberTextView();

//        //add button stuff
//        Button callNow = (Button) findViewById(R.id.buttonGoLog);
//        callNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//
//                    callNow();
//
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), "Your call has failed...",
//                            Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                    FirebaseCrash.report(new Exception("call failed, catch"));
//                }
//            }
//        });


    } //onCreate end

    private void callNow() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        myNumber = prefs.getString("prefsCallNumber", "123");

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Intent callingServiceIntent = new Intent(getApplicationContext(), CallingService.class);
            getApplicationContext().startService(callingServiceIntent);

            String uri = "tel:" + myNumber;
            Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
            Log.i("call -", "on click calling");
            startActivity(dialIntent);

            Toast.makeText(getApplicationContext(), "Calling..",
                    Toast.LENGTH_LONG).show();

            finish();  //not sure if i need to finish, it just brings it back to mainactivity anyways.
//                         tried commenting out to fix the problem where - call with no permission made doyoutest not come up and foregroind notif stays up

        } else {
            //get permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_CALL_PHONE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    this.recreate();

//                    callNow();



                } else {

                    Toast.makeText(this, "Call permission denied! Cannot call.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void cancelCurrentNotifications() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    } //cancel current notifications up top

    private void setNumberTextView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        myNumber = prefs.getString("prefsCallNumber", "123");

        TextView myNumberTv = (TextView) findViewById(R.id.myNumberTv);
        myNumberTv.setText(myNumber);
    } //get call number from sharedprefs and set the textview


} //main class end
