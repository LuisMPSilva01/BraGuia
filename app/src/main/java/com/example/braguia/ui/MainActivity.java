package com.example.braguia.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.databinding.ActivityMainBinding;
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
        try {
            userViewModel.getUser().observe(this, user -> {
                Log.e("main","user type="+user.getUser_type());
                if (user != null && Objects.equals(user.getUser_type(), "loggedOut")) {
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

                case R.id.favourites:
                    replaceFragment(new FavouritesFragment());
                    break;

                case R.id.discover:
                    replaceFragment(new DiscoverFragment());
                    break;

                case R.id.add_roadmap:
                    replaceFragment(new AddRoadMapFragment());
                    break;
            }

            Menu sb_menu = binding.sidebar.getMenu();
            for(int i = 0; i < sb_menu.size(); i++) {
                MenuItem sb_item = sb_menu.getItem(i);
                if(sb_item.isChecked()){
                    sb_item.setChecked(false);
                }
            }
            return true;
        });

        //left side bar
        binding.sidebar.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()){
                        case R.id.profile:
                            //Toast.makeText(MainActivity.this, "Profile Selected", Toast.LENGTH_LONG).show();
                            replaceFragment(new ProfileFragment());
                            break;
                        case R.id.emergency_contacts:
                            //Toast.makeText(MainActivity.this, "Emergency Contacts Selected", Toast.LENGTH_LONG).show();
                            replaceFragment(new EmergencyContactsFragment());
                            break;
                        case R.id.definitions:
                            //Toast.makeText(MainActivity.this, "Definitions Selected", Toast.LENGTH_LONG).show();
                            replaceFragment(new DefinitionsFragment());
                            break;
                        case R.id.localization:
                            //Toast.makeText(MainActivity.this, "Localization Selected", Toast.LENGTH_LONG).show();
                            replaceFragment(new LocalizationFragment());
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

                    Menu bnv_menu = binding.bottomNavigationView.getMenu();
                    for(int i = 0; i < bnv_menu.size(); i++) {
                        MenuItem bnv_item = bnv_menu.getItem(i);
                        bnv_item.setChecked(!bnv_item.isChecked());
                    }

                    // Close side bar
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                });
        //Allows side-bar items to be selected
        binding.sidebar.bringToFront();
    }

    private void changeToLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.conteudo,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
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
