package com.example.braguia.ui;

import static android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.databinding.ActivityMainBinding;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.viewmodel.UserViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Toolbar toolbar;
    private ActivityMainBinding binding;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle drawerToggle;
    private UserViewModel userViewModel;
    private LocationManager locationManager;

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;



    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean localizationSwitchSate = sharedPrefs.getBoolean("locationSwitch", false);

        //createNotification(R.drawable.uminho_logo, "Ganda titulo", "Ganda mensagem");

        Log.e("MainActivity","STARTACTIVITY");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        try {
            userViewModel.getUser().observe(this, user -> {
                Log.e("MainActivity","REFRESH");
                if (Objects.equals(user.getUser_type(), "loggedOff")) {
                    changeToLoginActivity();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        drawer_layout = findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close);
        drawer_layout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        replaceFragment(new HomeFragment());
        //Bottom navbar
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.discover:
                    replaceFragment(new DiscoverFragment());
                    break;

                case R.id.add_roadmap:
                    replaceFragment(new AddRoadMapFragment());
                    break;
            }

            Menu bnv_menu = binding.bottomNavigationView.getMenu();
            for(int i = 0; i < bnv_menu.size(); i++) {
                MenuItem bnv_item = bnv_menu.getItem(i);
                bnv_item.setChecked(!bnv_item.isChecked());
            }

            return true;
        });


        //left side bar
        binding.sidebar.setNavigationItemSelectedListener(

                menuItem -> {
                    switch (menuItem.getItemId()){
                        case R.id.profile:
                            replaceFragment(new ProfileFragment());
                            break;
                        case R.id.emergency_contacts:
                            replaceFragment(new ContactsListFragment());
                            break;
                        case R.id.socials_contacts:
                            replaceFragment(new SocialsListFragment());
                            break;
                        case R.id.partners_contacts:
                            replaceFragment(new PartnersListFragment());
                            break;
                        case R.id.localization:
                            return false;
                        case R.id.definitions:
                            Intent intent = new Intent(MainActivity.this, DefinitionsActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.logout:
                            try {
                                userViewModel.logOut(getApplicationContext(), new UserViewModel.LogOutCallback() {
                                    @Override
                                    public void onLogOutSuccess() {
                                        changeToLoginActivity();
                                    }
                                    @Override
                                    public void onLogOutFailure() {}
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                    }

                    Menu sb_menu = binding.sidebar.getMenu();
                    for(int i = 0; i < sb_menu.size(); i++) {
                        MenuItem sb_item = sb_menu.getItem(i);
                        if(sb_item.isChecked()){
                            sb_item.setChecked(false);
                        }
                    }
                    // Close side bar
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                });

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Localization switch event listener
        MenuItem menuItem = binding.sidebar.getMenu().findItem(R.id.localization);
        View view = menuItem.getActionView();
        SwitchCompat localization_switch = view.findViewById(R.id.localizationSwitch);
        // Set saved state
        localization_switch.setChecked(localizationSwitchSate);
        localization_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(10000);
                    locationRequest.setFastestInterval(10000/2);

                    LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();

                    locationSettingsRequestBuilder.addLocationRequest(locationRequest);
                    locationSettingsRequestBuilder.setAlwaysShow(true);

                    SettingsClient settingsClient = LocationServices.getSettingsClient(MainActivity.this);
                    Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());
                    task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            Toast.makeText(MainActivity.this, "GPS is ON.", Toast.LENGTH_SHORT).show();

                        }
                    });

                    task.addOnFailureListener(MainActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "GPS is OFF.", Toast.LENGTH_SHORT).show();

                            if (e instanceof ResolvableApiException) {
                                try {
                                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                    resolvableApiException.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sendIntentException) {
                                    sendIntentException.printStackTrace();
                                }
                            }
                        }
                    });
                    // Set switch state
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("locationSwitch", isChecked);
                    editor.apply();
                }
                else{
                    startActivity(new Intent(ACTION_LOCATION_SOURCE_SETTINGS));
                    Toast.makeText(MainActivity.this, "GPS is OFF.", Toast.LENGTH_SHORT).show();

                    // Set switch state
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("locationSwitch", isChecked);
                    editor.apply();
                }


            }
        });

        // Localization item not clickable
        MenuItem menuItem2 = binding.sidebar.getMenu().findItem(R.id.localization);
        SpannableString spannableString = new SpannableString(menuItem2.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
        menuItem2.setTitle(spannableString);
        menuItem2.setEnabled(false);

        //Allows side-bar items to be selected
        binding.sidebar.bringToFront();


}

    private void changeToLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.conteudo,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.localization) {// Return true to indicate that the item was selected, but do not perform any action
            return true;
        }// Allow the DrawerToggle to handle the click event if the clicked item is not R.id.identification
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        // Otherwise, let the default behavior handle the click event
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    public void createNotification(int image, String title, String message){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(image)
                .setContentTitle(title)
                .setContentText(message);
        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(1, notification);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
        }

    }
}
