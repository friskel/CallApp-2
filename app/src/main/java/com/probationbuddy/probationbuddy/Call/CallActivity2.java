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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.probationbuddy.probationbuddy.R;

public class CallActivity2 extends AppCompatActivity {
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

        cancelCurrentNotifications();
        setNumberTextView();

        //add button stuff
        Button callNow = (Button) findViewById(R.id.buttonGoLog);
        callNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent callingServiceIntent = new Intent(getApplicationContext(), CallingService.class);
                    getApplicationContext().startService(callingServiceIntent);


                    String uri = "tel:" + myNumber;
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                    Log.i("call -", "on click calling");
                    startActivity(dialIntent);
                    finish();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Your call has failed...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });







    } //onCreate end




    private void cancelCurrentNotifications(){
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    } //cancel current notifications up top

    private void setNumberTextView(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        myNumber = prefs.getString("prefsCallNumber", "123");

        TextView myNumberTv = (TextView) findViewById(R.id.myNumberTv);
        myNumberTv.setText(myNumber);
    } //get call number from sharedprefs and set the textview


} //main class end
