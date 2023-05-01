package com.example.braguia.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.example.braguia.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActivityMainBinding binding;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle drawerToggle;
    private UserViewModel userViewModel;
    private SwitchCompat localizSwitch;

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MenuItem localizationItem = binding.sidebar.getMenu().findItem(R.id.localization);
        localizationItem.setEnabled(false);
        SpannableString spanString = new SpannableString(localizationItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, spanString.length(), 0);
        localizationItem.setTitle(spanString);


        ///////////////////////////////////Codigo para criar notificações. TODO: Meter em função/////////////////////////////////////////////////////
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.uminho_logo)
                .setContentTitle("Ganda titulo")
                .setContentText("Ganda mensagem da notificação");
        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(1, notification);
        } else {
            // Solicite a permissão para o usuário
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
                            //Toast.makeText(MainActivity.this, "Profile Selected", Toast.LENGTH_LONG).show();
                            replaceFragment(new ProfileFragment());
                            break;
                        case R.id.emergency_contacts:
                            //Toast.makeText(MainActivity.this, "Emergency Contacts Selected", Toast.LENGTH_LONG).show();
                            replaceFragment(new EmergencyContactsFragment());
                            break;
                        case R.id.localization:
                            return true;
                        case R.id.definitions:
                            //Toast.makeText(MainActivity.this, "Definitions Selected", Toast.LENGTH_LONG).show();
                            replaceFragment(new DefinitionsFragment());
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

                    localizSwitch = findViewById(R.id.localizationSwitch);
                    localizSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if(isChecked){
                            Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                        }
                    });

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

}
