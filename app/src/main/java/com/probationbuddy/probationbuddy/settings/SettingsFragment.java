package com.probationbuddy.probationbuddy.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.View;
import android.widget.Toast;

import com.probationbuddy.probationbuddy.MainActivity;
import com.probationbuddy.probationbuddy.R;
import com.probationbuddy.probationbuddy.TestDoneActivity;
import com.probationbuddy.probationbuddy.call.CallActivity2;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    int minute;
    int hour;
    String time;
    String minuteString;
    boolean am12;
    boolean alarmsActive;
    String editText1;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        onSharedPreferenceChanged(sharedPrefs, getString(R.string.prefIntervalKey));
        onSharedPreferenceChanged(sharedPrefs, getString(R.string.prefsCallNumberKey));
        onSharedPreferenceChanged(sharedPrefs, getString(R.string.prefStartTimeKey));
        onSharedPreferenceChanged(sharedPrefs, getString(R.string.prefsColorNumberKey));
        onSharedPreferenceChanged(sharedPrefs, getString(R.string.prefsSound));

    }

    @Override
    public void onResume() {
        super.onResume();
        //register the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences prefs = getDefaultSharedPreferences(getContext());
        alarmsActive = prefs.getBoolean("prefsActivate", false);

        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }

        if (preference instanceof TimePreference) {
            TimePreference timePreference = (TimePreference) preference;


            //set hour and minute of morning start time
            minute = prefs.getInt("prefStartTime", 123) % 60;
            hour = prefs.getInt("prefStartTime", 123);
            hour = hour - minute;
            hour = hour / 60;

            am12 = false;

            minuteString = String.valueOf(minute);
            if (minute < 10) {
                minuteString = "0" + minute;
            }


            if (hour == 0) {
                hour = 12;
                am12 = true;

            }

            time = (hour + ":" + minuteString + " am");

            if (hour > 12) {
                hour = hour - 12;
                time = (hour + ":" + minuteString + " pm");
            }
            if (hour == 12) {
                time = (hour + ":" + minuteString + " pm");
                if (am12) {
                    time = (hour + ":" + minuteString + " am");
                }
            }


            preference.setSummary(time);


        }

        if (preference instanceof SwitchPreferenceCompat) {
            SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) preference;


            if (preference.getKey().equals("prefsActivate")) {
                //get shared prefs


                if (alarmsActive) {
                    //get shared prefs
                    //set hour and minute of morning start time
                    minute = prefs.getInt("prefStartTime", 123) % 60;
                    hour = prefs.getInt("prefStartTime", 123);
                    hour = hour - minute;
                    hour = hour / 60;

                    am12 = false;

                    minuteString = String.valueOf(minute);
                    if (minute < 10) {
                        minuteString = "0" + minute;
                    }


                    if (hour == 0) {
                        hour = 12;
                        am12 = true;

                    }

                    time = (hour + ":" + minuteString + " am");

                    if (hour > 12) {
                        hour = hour - 12;
                        time = (hour + ":" + minuteString + " pm");
                    }
                    if (hour == 12) {
                        time = (hour + ":" + minuteString + " pm");
                        if (am12) {
                            time = (hour + ":" + minuteString + " am");
                        }
                    }


                    Toast.makeText(getContext(), "Reminders will start at " + time + "!",
                            Toast.LENGTH_LONG).show();

                    Activity main = getActivity();
                    if(main instanceof MainActivity) {
                        ((MainActivity) main).openSnackbar();
                    }

                }

                if (!alarmsActive) {
                    Toast.makeText(getContext(), "Probation Buddy has been disabled!",
                            Toast.LENGTH_SHORT).show();

                    Activity main = getActivity();
                    if(main instanceof MainActivity) {
                        ((MainActivity) main).openSnackbar();
                    }

                }


            }
        }

        if (preference instanceof EditTextPreference) {

            //get shared prefs
            editText1 = prefs.getString(key, "defaultz");
            preference.setSummary(editText1);


        } else {
//            preference.setSummary(sharedPreferences.getString(key, ""));
            //prevented 'class cast string to int' crash by commenting out

        }
    }

    private void showDisabledSnackbar() {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean haveTestToday = sharedPrefs.getBoolean("haveTestToday", false);

        if (haveTestToday){
            Snackbar.make(getActivity().findViewById(android.R.id.content), "You have to test today!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Done", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intentDone = new Intent(getActivity(), TestDoneActivity.class);
                            startActivity(intentDone);
                        }

                    }) //done opens DoYouTest
                    .setActionTextColor(Color.RED)
                    .show();

        }else {

            Snackbar.make(getActivity().findViewById(android.R.id.content), "Reminders are disabled.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Call Now", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String callNumber = sharedPrefs.getString("prefsCallNumber", "not set!");
                            if (callNumber.equals("not set!") || callNumber.equals("")) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Hold on..")
                                        .setMessage("You need to set your call-in number first before making a call!  ")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intentRestartMain = new Intent(getActivity(), MainActivity.class);

                                                startActivity(intentRestartMain);
                                            }
                                        })
                                        .show();


                            } else {

                                Intent intentCall = new Intent(getContext(), CallActivity2.class);
                                intentCall.putExtra("callNow", true);
                                startActivity(intentCall);
                            }
                        }

                    })
                    .setActionTextColor(Color.GREEN)
                    .show();
        }
    }

    private void showEnabledSnackbar(){
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean haveTestToday = sharedPrefs.getBoolean("haveTestToday", false);


            Snackbar.make(getActivity().findViewById(android.R.id.content), "You have not called today!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Call Now", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String callNumber = sharedPrefs.getString("prefsCallNumber", "not set!");
                        if (callNumber.equals("not set!") || callNumber.equals("")){
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Hold on..")
                                    .setMessage("You need to set your call-in number first before making a call!  ")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intentRestartMain = new Intent(getActivity(), MainActivity.class);

                                            startActivity(intentRestartMain);
                                        }
                                    })
                                    .show();


                        }else {

                            Intent intentCall = new Intent(getContext(), CallActivity2.class);
                            intentCall.putExtra("callNow", true);
                            startActivity(intentCall);
                        }
                    }

                })
                .setActionTextColor(Color.RED)
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // Try if the preference is one of our custom Preferences
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            // Create a new instance of TimePreferenceDialogFragment with the key of the related
            // Preference
            dialogFragment = TimePreferenceDialogFragmentCompat
                    .newInstance(preference.getKey());
        }

        // If it was one of our cutom Preferences, show its dialog
        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(),
                    "android.support.v7.preference" +
                            ".PreferenceFragment.DIALOG");
        }
        // Could not be handled here. Try with the super method.
        else {
            super.onDisplayPreferenceDialog(preference);
        }
    }


}
