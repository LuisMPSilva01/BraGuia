package com.example.braguia.ui.Activitys;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.braguia.R;
import com.example.braguia.model.trails.Edge;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.ui.TrailDescriptionFragment;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.braguia.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapsActivity extends Fragment {
    final int id;
    private GoogleMap mMap;
    private String TAG = "MAPS";
    TrailViewModel trailViewModel;

    public MapsActivity(int id){
        this.id=id;
    }

    public static MapsActivity newInstance(int id) {
        return new MapsActivity(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);

        trailViewModel.getTrailById(1).observe(getViewLifecycleOwner(), trail -> {
            // Assuming the "trail" object contains information about the trail
            // and its coordinates, you can proceed to display it on the map.
            if(trail!=null) {
                displayTrailOnMap(trail);
            }
        });
        return view;
    }

    public interface OnTrailCoordinatesLoadedListener {
        void onTrailCoordinatesLoaded(List<LatLng> coordinates);
    }
    public void loadMap(GoogleMap googleMap,Trail trail) {
        mMap = googleMap;

        List<EdgeTip> edgeTips = trail.getEdges().stream()
                .flatMap(e -> Stream.of(e.getEdge_start(), e.getEdge_end()))
                .collect(Collectors.toList());

        List<String> wayPointsAPI = new ArrayList<>();
        LatLng origem = edgeTips.get(id).getMapsCoordinate();
        for(EdgeTip edgeTip:edgeTips){
            wayPointsAPI.add(edgeTip.getMapsCoordinateString());
            mMap.addMarker(new MarkerOptions().position(edgeTip.getMapsCoordinate()).title(edgeTip.getPin_name()));
        }

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBLjQ5MIahmpuqpD3syyt67Umc2ZFAUmJk")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, wayPointsAPI.get(0), wayPointsAPI.get(wayPointsAPI.size()-1))
                .waypoints(wayPointsAPI.toArray(new String[0])) // Add the waypoints
                .optimizeWaypoints(true); // Optimize the order of waypoints

        try {
            DirectionsResult res = req.await();

            // Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            // Decode polyline and add points to the list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        // Decode polyline and add points to the list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error occurred: ");
            ex.printStackTrace();
        }
        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origem, 25));
    }

    private void displayTrailOnMap(Trail trail) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            loadMap(googleMap, trail);
        });
    }
}
