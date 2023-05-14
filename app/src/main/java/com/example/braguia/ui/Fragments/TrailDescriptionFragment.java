package com.example.braguia.ui.Fragments;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.ui.Activitys.MainActivity;
import com.example.braguia.ui.Activitys.NavigationActivity;
import com.example.braguia.ui.Activitys.NotificationPinScreenActivity;
import com.example.braguia.ui.Services.Trip;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.ui.Activitys.NotificationPinScreenActivity;
import com.example.braguia.ui.Services.Trip;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrailDescriptionFragment extends Fragment {
    private TrailViewModel trailViewModel;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id = getArguments().getInt("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!MapsFragment.meetsPreRequisites(getContext())) {
            Toast toast = Toast.makeText(getContext(), "Instale o Google Maps para poder navegar", Toast.LENGTH_SHORT);
            toast.show();
        }
        View view = inflater.inflate(R.layout.fragment_trail_description, container, false);

        trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        trailViewModel.getTrailById(id).observe(getViewLifecycleOwner(), x -> loadView(view, x));
        return view;
    }

    private void loadView(View view, Trail trail) {
        TextView titulo = view.findViewById(R.id.trail_description_title);
        titulo.setText(trail.getTrail_name());

        ImageView imagem = view.findViewById(R.id.trailImageDescription);
        Picasso.get().load(trail.getTrail_img()
                        .replace("http", "https"))
                .into(imagem);
        Button intro = view.findViewById(R.id.start_trip_button);

        intro.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), NavigationActivity.class);
            intent.putExtra("id", trail.getId()); // add a string extra
            startActivity(intent);
        });


        FragmentManager childFragmentManager = getChildFragmentManager();
        EdgeListFragment childFragment = EdgeListFragment.newInstanceByTrails(new ArrayList<>(List.of(id)));
        FragmentTransaction transaction1 = childFragmentManager.beginTransaction();
        transaction1.add(R.id.pin_list_content, childFragment);
        transaction1.commit();


        MapsFragment mapsFragment = MapsFragment.newInstance(trail.getRoute());
        FragmentTransaction transaction2 = childFragmentManager.beginTransaction();
        transaction2.replace(R.id.maps_trail_overview, mapsFragment);
        transaction2.commit();
    }
}

