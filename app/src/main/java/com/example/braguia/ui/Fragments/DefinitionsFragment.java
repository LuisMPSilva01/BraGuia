package com.example.braguia.ui.Fragments;

import static android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.braguia.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

public class DefinitionsFragment extends Fragment {

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private SwitchCompat locationSwitch;
    public DefinitionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_definitions, container, false);
        handleLocationSwitch(view);
        handleDarkModeSwitch(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(locationSwitch!=null){
            LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            locationSwitch.setChecked(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        }
    }

    void handleLocationSwitch(View view){
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        SwitchCompat localization_switch = view.findViewById(R.id.localization_switch);
        locationSwitch= localization_switch;
        locationSwitch.setChecked(isGPSEnabled);
        locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle the switch state change here
            if (isChecked){
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(10000/2);

                LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();

                locationSettingsRequestBuilder.addLocationRequest(locationRequest);
                locationSettingsRequestBuilder.setAlwaysShow(true);

                SettingsClient settingsClient = LocationServices.getSettingsClient(requireActivity());
                Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());

                task.addOnFailureListener(requireActivity(), e -> {

                    if (e instanceof ResolvableApiException) {
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                    }
                });
            }
            else{
                startActivity(new Intent(ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
    }

    void handleDarkModeSwitch(View view){
        SharedPreferences sharedPrefs = requireActivity().getSharedPreferences("BraguiaPreferences", Context.MODE_PRIVATE);
        SwitchCompat dark_mode_switch = view.findViewById(R.id.dark_mode_switch);

        boolean isDarkModeEnabled = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES;

        dark_mode_switch.setChecked(isDarkModeEnabled);

        dark_mode_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle the switch state change here
            if (isChecked) {
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("darkModeSwitchState", true);
                editor.apply();

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(getContext(), "Dark mode enabled", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("darkModeSwitchState", false);
                editor.apply();

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(requireActivity(), "Dark mode disabled", Toast.LENGTH_SHORT).show();
            }

        });
    }
}