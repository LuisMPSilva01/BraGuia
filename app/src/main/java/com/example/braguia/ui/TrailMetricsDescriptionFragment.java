package com.example.braguia.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailMetricsDescriptionFragment extends Fragment {
    private final int id;
    public TrailMetricsDescriptionFragment(int id){
        this.id=id;
    }

    public static TrailMetricsDescriptionFragment newInstance(int id) {
        return new TrailMetricsDescriptionFragment(id);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trail_metrics_description, container, false);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        TrailViewModel trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        userViewModel.getMetricsById(id).observe(getViewLifecycleOwner(), trailMetrics -> {
            trailViewModel.getTrailById(trailMetrics.getTrail_id()).observe(getViewLifecycleOwner(), trail -> {
                if(trail!=null){
                    loadView(view,trail,trailMetrics);
                }
            });
        });

        return view;
    }

    private void loadView(View view, Trail trail, TrailMetrics metrics){
        FragmentManager childFragmentManager = getChildFragmentManager();
        PinListFragment childFragment = PinListFragment.newInstance(new ArrayList<>(List.of(trail.getId())));
        FragmentTransaction transaction1 = childFragmentManager.beginTransaction();
        transaction1.add(R.id.trail_metricspin_list_content, childFragment);
        transaction1.commit();

        TextView titulo = view.findViewById(R.id.trail_metrics_description_title);
        titulo.setText(trail.getTrail_name());

        ImageView imagem = view.findViewById(R.id.trail_metricsImageDescription);
        Picasso.get().load(trail.getTrail_img()
                        .replace("http", "https"))
                .into(imagem);
    }
}
