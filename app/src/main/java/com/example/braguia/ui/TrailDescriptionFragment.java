package com.example.braguia.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrailDescriptionFragment extends Fragment {
    private TrailViewModel trailViewModel;
    private final int id;
    public TrailDescriptionFragment(int id){
        this.id=id;
    }

    public static TrailDescriptionFragment newInstance(int id) {
        return new TrailDescriptionFragment(id);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(!MapsFragment.meetsPreRequisites(getContext())){
            Toast toast = Toast.makeText(getContext(), "Instale o Google Maps para poder navegar", Toast.LENGTH_SHORT);
            toast.show();
        }

        View view = inflater.inflate(R.layout.fragment_trail_description, container, false);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        try {
            userViewModel.updateTrailHistory(id);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        trailViewModel.getTrailById(id).observe(getViewLifecycleOwner(), x -> loadView(view, x));


        FragmentManager childFragmentManager = getChildFragmentManager();
        PinListFragment childFragment = PinListFragment.newInstance(new ArrayList<>(List.of(id)));
        FragmentTransaction transaction1 = childFragmentManager.beginTransaction();
        transaction1.add(R.id.pin_list_content, childFragment);
        transaction1.commit();

        MapsFragment mapsFragment = MapsFragment.newInstance(id);
        FragmentTransaction transaction2 = childFragmentManager.beginTransaction();
        transaction2.replace(R.id.maps_trail_overview, mapsFragment);
        transaction2.commit();
        return view;
    }

    private void loadView(View view, Trail trail){
        TextView titulo = view.findViewById(R.id.trail_description_title);
        titulo.setText(trail.getTrail_name());

        ImageView imagem = view.findViewById(R.id.trailImageDescription);
        Picasso.get().load(trail.getTrail_img()
                        .replace("http", "https"))
                        .into(imagem);
        Button intro = view.findViewById(R.id.start_trip_button);
        intro.setOnClickListener(v -> startNavigation(trail));
    }

    private void startNavigation(Trail trail) {
        // Create a new instance of the destination fragment
        if(MapsFragment.meetsPreRequisites(getContext())){
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
                Toast.makeText(getContext(), "Instale o Google Maps para poder navegar", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast toast = Toast.makeText(getContext(), "Instale o Google Maps para poder navegar", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

