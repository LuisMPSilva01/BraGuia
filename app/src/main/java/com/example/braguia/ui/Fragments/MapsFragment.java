package com.example.braguia.ui.Fragments;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.braguia.R;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.ui.MapsUtilils.GetPathFromLocation;
import com.example.braguia.viewmodel.TrailViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;
    private final String TAG = "MAPS";
    TrailViewModel trailViewModel;
    final List<EdgeTip> edgeTips;

    public MapsFragment(List<EdgeTip> edgeTips){
        this.edgeTips=edgeTips;
    }

    public static MapsFragment newInstance(List<EdgeTip> edgeTips) {
        return new MapsFragment(edgeTips);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps_overview, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            loadMap(googleMap);
        });
        return view;
    }
    public void loadMap(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<LatLng> wayPointsAPI = new ArrayList<>();
        for(EdgeTip edgeTip:edgeTips){
            wayPointsAPI.add(edgeTip.getMapsCoordinate());
            mMap.addMarker(new MarkerOptions().position(edgeTip.getMapsCoordinate()).title(edgeTip.getPin_name()));
        }
        LatLng source = wayPointsAPI.get(0);

        if(wayPointsAPI.size()>=2) {
            LatLng destination = wayPointsAPI.get(wayPointsAPI.size() - 1);
            wayPointsAPI.remove(0);
            wayPointsAPI.remove(wayPointsAPI.size() - 1);

            new GetPathFromLocation(getActivity(), source, destination, wayPointsAPI, mMap, false, false, polyLine -> {
                polyLine.color(R.color.teal_200);
                mMap.addPolyline(polyLine);
            }).execute();
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 12));
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
