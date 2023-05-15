package com.example.braguia.ui.Activitys;

import static android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.braguia.R;
import com.example.braguia.databinding.ActivityMainBinding;
import com.example.braguia.model.user.User;
import com.example.braguia.viewmodel.UserViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
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

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkLoggedIn();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        drawer_layout = findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close);
        drawer_layout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the Bottom Navigation View with the NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        configureBottomNavigation(navController);
        configureSideBar(navController);
        configureLocationButton();
    }

    public void checkLoggedIn(){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        try {
            LiveData<User> userLiveData = userViewModel.getUser();

            userLiveData.observe(this, user -> {
                if(user!=null){
                    if (Objects.equals(user.getUser_type(), "loggedOff")) {
                        changeToLoginActivity();
                    }
                    userLiveData.removeObservers(this);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configureBottomNavigation(NavController navController){
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()== R.id.home){
                navController.navigate(R.id.homeFragment);
            } else if(item.getItemId()== R.id.discover){
                navController.navigate(R.id.discoverFragment);
            }
            return true;
        });
    }

    private void configureSideBar(NavController navController){
        binding.sidebar.setNavigationItemSelectedListener(

                menuItem -> {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.profile) {
                        navController.navigate(R.id.profileFragment);
                    } else if (itemId == R.id.emergency_contacts) {
                        navController.navigate(R.id.contactsListFragment);
                    } else if (itemId == R.id.socials_contacts) {
                        navController.navigate(R.id.socialsListFragment);
                    } else if (itemId == R.id.partners_contacts) {
                        navController.navigate(R.id.partnersListFragment);
                    } else if (itemId == R.id.localization) {
                        return false;
                    } else if (itemId == R.id.definitions) {
                        Intent intent = new Intent(MainActivity.this, DefinitionsActivity.class);
                        startActivity(intent);
                    } else if (itemId == R.id.logout) {
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
                    }

                    Menu sb_menu = binding.sidebar.getMenu();
                    for (int i = 0; i < sb_menu.size(); i++) {
                        MenuItem sb_item = sb_menu.getItem(i);
                        if (sb_item.isChecked()) {
                            sb_item.setChecked(false);
                        }
                    }
                    // Close side bar
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
        );
        //Allows side-bar items to be selected
        binding.sidebar.bringToFront();
    }

    private void configureLocationButton(){
        SharedPreferences sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean localizationSwitchSate = sharedPrefs.getBoolean("locationSwitch", false);

        // Localization switch event listener
        MenuItem menuItem = binding.sidebar.getMenu().findItem(R.id.localization);
        View view = menuItem.getActionView();
        SwitchCompat localization_switch = view.findViewById(R.id.localizationSwitch);
        // Set saved state
        localization_switch.setChecked(localizationSwitchSate);
        localization_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
                task.addOnSuccessListener(MainActivity.this, locationSettingsResponse -> Toast.makeText(MainActivity.this, "GPS is ON.", Toast.LENGTH_SHORT).show());

                task.addOnFailureListener(MainActivity.this, e -> {
                    Toast.makeText(MainActivity.this, "GPS is OFF.", Toast.LENGTH_SHORT).show();

                    if (e instanceof ResolvableApiException) {
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
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
                Toast.makeText(MainActivity.this, "GPS is OFF.", Toast.LENGTH_SHORT).show();

                // Set switch state
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("locationSwitch", false);
                editor.apply();
            }
        });

        // Localization item not clickable
        MenuItem menuItem2 = binding.sidebar.getMenu().findItem(R.id.localization);
        SpannableString spannableString = new SpannableString(menuItem2.getTitle());
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
        menuItem2.setTitle(spannableString);
        menuItem2.setEnabled(false);
    }
    private void changeToLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
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
}
