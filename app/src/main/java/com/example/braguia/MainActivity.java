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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.braguia.databinding.ActivityMainBinding;
import com.example.braguia.objects.Trail;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ActivityMainBinding binding;
    private TrailViewModel trailViewModel;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RecyclerView recyclerView = findViewById(R.id.recyclerview);
        //final TrailListAdapter adapter = new TrailListAdapter(new TrailListAdapter.TrailDiff());
        //recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

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



        // Left Side Navbar
        binding.NavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.profile:
                        //Toast.makeText(MainActivity.this, "Profile Selected", Toast.LENGTH_LONG).show();
                        replaceFragment(new ProfileFragment());
                        break;
                    case R.id.discover:
                        //Toast.makeText(MainActivity.this, "Discover Selected", Toast.LENGTH_LONG).show();
                        replaceFragment(new DiscoverFragment());
                        break;
                    case R.id.home:
                        //Toast.makeText(MainActivity.this, "Home Selected", Toast.LENGTH_LONG).show();
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.favourites:
                        //Toast.makeText(MainActivity.this, "Favourites Selected", Toast.LENGTH_LONG).show();
                        replaceFragment(new FavouritesFragment());
                        break;
                    case R.id.logout:
                        //Toast.makeText(MainActivity.this, "LogOut Selected", Toast.LENGTH_LONG).show();
                        replaceFragment(new LogoutFragment());
                        break;
                }
                return false;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}