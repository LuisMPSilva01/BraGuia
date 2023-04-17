package com.example.braguia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.braguia.databinding.ActivityMainBinding;
import com.example.braguia.objects.Trail;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle drawerToggle;
    private TrailViewModel trailViewModel;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawer_layout = findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close);
        drawer_layout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get a new or existing ViewModel from the ViewModelProvider.
        //this.trailViewModel = new ViewModelProvider(this).get(TrailViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        //try {
        //    // Update the cached copy of the words in the adapter.
        //    trailViewModel.getAllTrails().observe(this, adapter::submitList);
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}

        //replaceFragment(new HomeFragment());

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
            return true;
        });

        //left side bar
        binding.navigationView.setNavigationItemSelectedListener(
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
                            //Toast.makeText(MainActivity.this, "LogOut Selected", Toast.LENGTH_LONG).show();
                            replaceFragment(new LogoutFragment());
                            break;
                    }
                    // Close side bar
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                });
        //Allows side-bar items to be selected
        binding.navigationView.bringToFront();




    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
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

}
