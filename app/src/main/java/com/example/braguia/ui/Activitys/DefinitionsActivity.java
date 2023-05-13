package com.example.braguia.ui.Activitys;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.braguia.R;
import com.example.braguia.databinding.ActivityMainBinding;

import java.util.Objects;

public class DefinitionsActivity extends AppCompatActivity {

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


        SwitchCompat notification_switch = findViewById(R.id.notifications_switch);
        SwitchCompat dark_mode_switch = findViewById(R.id.dark_mode_switch);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Initialize switch states
        notification_switch.setChecked(notificationSwitchState);
        dark_mode_switch.setChecked(darkModeSwitchState);


        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the switch state change here
                if (isChecked) {
                    //notificationManager.cancelAll();
                } else {
                    //notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                }
                // Save switch state
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("notificationSwitchState", isChecked);
                editor.apply();
            }

        });
        dark_mode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            }
        });
    }

}