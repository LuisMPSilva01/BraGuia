package com.example.braguia.ui.Activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {


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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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
        checkDarkMode();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLoggedIn();
    }
    public void checkLoggedIn(){
        try {
            LiveData<User> userLiveData = userViewModel.getUser();
            userLiveData.observe(this, user -> {
                if(user!=null){
                    if (Objects.equals(user.getUser_type(), "loggedOff")) {
                        Log.e("MAIN","failed login");
                        changeToLoginActivity();
                    }
                    else Log.e("MAIN","login success");
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
                        navController.navigate(R.id.emergencyContactsFragment);
                    } else if (itemId == R.id.socials_contacts) {
                        navController.navigate(R.id.socialsListFragment);
                    } else if (itemId == R.id.partners_contacts) {
                        navController.navigate(R.id.partnersListFragment);
                    } else if (itemId == R.id.definitions) {
                        navController.navigate(R.id.definitionsFragment);
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

    private void changeToLoginActivity(){
        Log.e("DEBUG","CHANGE TO LOGIN ACTIVITY");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void checkDarkMode(){
        boolean wantsDarkMode = userViewModel.getDarkModePreference(this);
        boolean isDarkModeEnabled = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES;
        if(wantsDarkMode && !isDarkModeEnabled){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if(!wantsDarkMode && isDarkModeEnabled)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
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

    public boolean isDarkModeEnabled() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
