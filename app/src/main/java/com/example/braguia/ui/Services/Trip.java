package com.example.braguia.ui.Services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.ui.MainActivity;
import com.example.braguia.ui.Services.LocationTracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Trip {
    private long timeStart;
    private long distance;
    private int trailId;
    private List<EdgeTip> percorridos;
    private List<EdgeTip> previstos;
    private LocationTracker locationTracker;
    private LocationTracker.PinCallBack pinCallBack;

    public Trip(Trail trail, LocationTracker.PinCallBack pinCallBack) {
        this.timeStart = new Date().getTime();
        this.distance = 0;
        this.trailId=trail.getId();
        this.percorridos= new ArrayList<>();
        this.previstos=trail.getRoute();
        this.pinCallBack = pinCallBack;
    }

    public void start(Context context){
        locationTracker = new LocationTracker(context, this, pinCallBack);
        if (!locationTracker.canGetLocation()) {
            locationTracker.showSettingsAlert();
        }
    }


    public TrailMetrics finish(String username) {
        float percentageCompletion = (float) percorridos.size()/(previstos.size()+percorridos.size());
        float timeTaken = (float) (new Date().getTime() - timeStart)/1000; //returns in seconds
        List<Integer> p= percorridos.stream().map(EdgeTip::getId).collect(Collectors.toList());
        locationTracker.stopListener();
        return new TrailMetrics(username,trailId,percentageCompletion,timeTaken,p);
    }

    public EdgeTip verifyPins(Location location) {
        for (EdgeTip pin:previstos){
            float distance = pin.getLocation().distanceTo(location);
            if(distance <= 50){
                percorridos.add(pin);
                previstos.remove(pin);
                return pin;
            }
        }
        return null;
    }
}
