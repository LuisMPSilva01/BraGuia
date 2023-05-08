package com.example.braguia.ui;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.braguia.R;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.ui.MapsUtilils.DirectionPointListener;
import com.example.braguia.ui.MapsUtilils.GetPathFromLocation;
import com.example.braguia.viewmodel.TrailViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapsFragment extends Fragment {
    final int id;
    private GoogleMap mMap;
    private String TAG = "MAPS";
    TrailViewModel trailViewModel;

    public MapsFragment(int id){
        this.id=id;
    }

    public static MapsFragment newInstance(int id) {
        return new MapsFragment(id);
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
        requireActivity().getSupportFragmentManager().popBackStack();
        trailViewModel.getTrailById(1).observe(getViewLifecycleOwner(), trail -> {
            if(trail!=null) {
                displayTrailOnMap(trail);
            }
        });
        return view;
    }
    public void loadMap(GoogleMap googleMap,Trail trail) {
        mMap = googleMap;

        List<EdgeTip> edgeTips = trail.getEdges().stream()
                .flatMap(e -> Stream.of(e.getEdge_start(), e.getEdge_end()))
                .distinct()
                .collect(Collectors.toList());

        ArrayList<LatLng> wayPointsAPI = new ArrayList<>();
        for(EdgeTip edgeTip:edgeTips){
            wayPointsAPI.add(edgeTip.getMapsCoordinate());
            mMap.addMarker(new MarkerOptions().position(edgeTip.getMapsCoordinate()).title(edgeTip.getPin_name()));
        }

        LatLng source = wayPointsAPI.get(0);
        LatLng destination = wayPointsAPI.get(wayPointsAPI.size()-1);
        wayPointsAPI.remove(0);
        wayPointsAPI.remove(wayPointsAPI.size()-1);

        new GetPathFromLocation(getActivity(), source, destination, wayPointsAPI, mMap, false, false, polyLine ->{
            polyLine.color(R.color.teal_200);
            mMap.addPolyline(polyLine);
        }).execute();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 18));
    }

    private void displayTrailOnMap(Trail trail) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            loadMap(googleMap, trail);
        });
    }

    public static boolean meetsPreRequisites(Context context){
        PackageManager pm = context.getPackageManager();
        return isPackageInstalled("com.google.android.apps.maps", pm);
    }

    private static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
