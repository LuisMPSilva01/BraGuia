package com.example.braguia.ui.Activitys;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.ui.Fragments.MapsFragment;
import com.example.braguia.ui.Fragments.PinListFragment;
import com.example.braguia.ui.Services.LocationTracker;
import com.example.braguia.ui.Services.Servico;
import com.example.braguia.ui.Services.Trip;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NavigationActivity extends AppCompatActivity {
    public NavigationActivity(){
    }

    public static NavigationActivity newInstance() {
        return new NavigationActivity();
    }

    private Integer trailId;
    private LocationTracker locationTracker;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        if(trailId!=null){
            TrailViewModel trailViewModel = new ViewModelProvider(this).get(TrailViewModel.class);
            LiveData<Trail> trailLiveData=trailViewModel.getTrailById(trailId);
            trailLiveData.observe(this, trail -> {
                if(trail!=null){
                    load(trail);
                    trailLiveData.removeObservers(this);
                }
            });
        }
        if(getIntent()!=null) {
            if (getIntent().hasExtra("trip")) {
                Trip trip = (Trip) getIntent().getSerializableExtra("trip");
                load(trip.getTrail());
            }
            else if (getIntent().hasExtra("id")) {
                trailId = getIntent().getIntExtra("id",0);

                TrailViewModel trailViewModel = new ViewModelProvider(this).get(TrailViewModel.class);
                LiveData<Trail> trailLiveData=trailViewModel.getTrailById(trailId);
                trailLiveData.observe(this, trail -> {
                    if(trail!=null){
                        Trip trip = new Trip(trail);

                        Intent serviceIntent = new Intent(this, Servico.class);
                        serviceIntent.putExtra("trip", trip);
                        startForegroundService(serviceIntent);
                        startNavigation(trail);
                        load(trail);
                        trailLiveData.removeObservers(this);
                    }
                });
            }
        }
    }

    private void load(Trail trail){
        TextView titulo = findViewById(R.id.trail_description_title);
        titulo.setText(trail.getTrail_name());

        ImageView imagem = findViewById(R.id.trailImageDescription);
        Picasso.get().load(trail.getTrail_img()
                        .replace("http", "https"))
                .into(imagem);

        Button stop = findViewById(R.id.start_trip_button);
        stop.setOnClickListener(v -> {
            endNavigation();
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        MapsFragment mapsFragment = MapsFragment.newInstance(trail.getRoute());
        FragmentTransaction transaction2 = fragmentManager.beginTransaction();
        transaction2.replace(R.id.maps_trail_overview, mapsFragment);
        transaction2.commit();
    }
    private void startNavigation(Trail trail) {
        // Create a new instance of the destination fragment
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);

        if (MapsFragment.meetsPreRequisites(this)) {
            List<String> route = trail.getRoute().stream()
                    .map(EdgeTip::getLocationString)
                    .collect(Collectors.toList());

            int lastIndex = route.size() - 1;
            String destination = route.get(lastIndex);
            String waypoints = String.join("|", route.subList(0, lastIndex));

            Uri mapIntentUri = Uri.parse("google.navigation:q=" + destination
                    + Uri.decode("&waypoints=" + waypoints));


            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            try {
                startActivity(mapIntent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "Instale o Google Maps para poder navegar", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast toast = Toast.makeText(this, "Instale o Google Maps para poder navegar", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void endNavigation() {
        Intent serviceIntent = new Intent(this, Servico.class);
        stopService(serviceIntent);

        Toast.makeText(this, "Métricas registadas no histórico", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", trailId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.trailId = savedInstanceState.getInt("id");
    }
}
