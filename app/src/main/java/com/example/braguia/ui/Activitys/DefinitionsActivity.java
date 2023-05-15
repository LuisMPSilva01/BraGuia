package com.example.braguia.ui.Activitys;

import static android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.braguia.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

public class DefinitionsActivity extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    public DefinitionsActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definitions);

        // Retrieve the saved switch states from shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean notificationSwitchState = sharedPrefs.getBoolean("notificationSwitchState", false);
        boolean darkModeSwitchState = sharedPrefs.getBoolean("darkModeSwitchState", false);


        SwitchCompat localization_switch = findViewById(R.id.localization_switch);
        SwitchCompat dark_mode_switch = findViewById(R.id.dark_mode_switch);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Initialize switch states
        localization_switch.setChecked(notificationSwitchState);
        dark_mode_switch.setChecked(darkModeSwitchState);


        localization_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle the switch state change here
            if (isChecked){
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(10000/2);

                LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();

                locationSettingsRequestBuilder.addLocationRequest(locationRequest);
                locationSettingsRequestBuilder.setAlwaysShow(true);

                SettingsClient settingsClient = LocationServices.getSettingsClient(DefinitionsActivity.this);
                Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());
                task.addOnSuccessListener(DefinitionsActivity.this, locationSettingsResponse -> Toast.makeText(DefinitionsActivity.this, "GPS is ON.", Toast.LENGTH_SHORT).show());

                task.addOnFailureListener(DefinitionsActivity.this, e -> {
                    Toast.makeText(DefinitionsActivity.this, "GPS is OFF.", Toast.LENGTH_SHORT).show();

                    if (e instanceof ResolvableApiException) {
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(DefinitionsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                    }
                });
                // Set switch state
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("locationSwitch", true);
                editor.apply();
            }
            else{
                startActivity(new Intent(ACTION_LOCATION_SOURCE_SETTINGS));
                Toast.makeText(DefinitionsActivity.this, "GPS is OFF.", Toast.LENGTH_SHORT).show();

                // Set switch state
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("locationSwitch", false);
                editor.apply();
            }
        });
        dark_mode_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle the switch state change here
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            // Save switch state
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean("darkModeSwitchState", isChecked);
            editor.apply();
        });
    }

}