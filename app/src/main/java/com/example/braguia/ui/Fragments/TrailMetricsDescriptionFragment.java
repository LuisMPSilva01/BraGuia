package com.example.braguia.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;

public class TrailMetricsDescriptionFragment extends Fragment {
    private int id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id = getArguments().getInt("id");
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
        PinListFragment childFragment = PinListFragment.newInstanceByPins(metrics.getPinIdList());
        FragmentTransaction transaction1 = childFragmentManager.beginTransaction();
        transaction1.add(R.id.trail_metricspin_list_content, childFragment);
        transaction1.commit();

        TextView titulo = view.findViewById(R.id.trail_metrics_description_title);
        titulo.setText(trail.getTrail_name());

        TextView timeTaken = view.findViewById(R.id.trail_metricsTImeTaken);
        String timeTakenString = "Time Taken: " + metrics.getTimeTaken() + " segundos";
        timeTaken.setText(timeTakenString);

        ImageView imagem = view.findViewById(R.id.trail_metricsImageDescription);
        Picasso.get().load(trail.getTrail_img()
                        .replace("http", "https"))
                .into(imagem);
    }
}
