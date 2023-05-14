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
<<<<<<< Updated upstream
        if (!MapsFragment.meetsPreRequisites(getContext())) {
            Toast toast = Toast.makeText(getContext(), "Instale o Google Maps para poder navegar", Toast.LENGTH_SHORT);
=======
        if(!MapsFragment.meetsPreRequisites(getContext())){
            Toast toast = Toast.makeText(getContext(), "Install Google Maps to navigate", Toast.LENGTH_SHORT);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======

    private void startNavigation(Trail trail) {
        // Create a new instance of the destination fragment
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);

        createNotification(trail.getRoute().get(0));
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
                Toast.makeText(getContext(), "Install Google Maps to navigate", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast toast = Toast.makeText(getContext(), "Install Google Maps to navigate", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void endNavigation(Trip trip) {
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.addMetrics(trip);
        Toast.makeText(getContext(), "Metrics registed in the history", Toast.LENGTH_LONG).show();
    }

    public void createNotification(EdgeTip edgeTip){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = requireActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity(), "channel_id")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(edgeTip.getPin_name())
                .setContentText(edgeTip.getPin_desc());

        // Add action button
        Intent intent = new Intent(requireActivity(), NotificationPinScreenActivity.class);
        intent.putExtra("EdgeTip", edgeTip);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(requireActivity());
        stackBuilder.addNextIntentWithParentStack(intent);
            // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(resultPendingIntent);


        Notification notification = builder.build();
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(requireActivity());
            notificationManagerCompat.notify(1, notification);
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save any necessary data into the outState Bundle object
    }
>>>>>>> Stashed changes
}

