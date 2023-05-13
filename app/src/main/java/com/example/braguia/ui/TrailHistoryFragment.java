package com.example.braguia.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class TrailHistoryFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    public TrailHistoryFragment() {
    }

    public static TrailHistoryFragment newInstance(int columnCount) {
        TrailHistoryFragment fragment = new TrailHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trail_item_list, container, false);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        TrailViewModel trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        userViewModel.getMetrics().observe(getViewLifecycleOwner(), metrics -> {
            if(metrics!=null && metrics.size()>0){
                trailViewModel.getTrailsById(metrics.stream()
                        .map(TrailMetrics::getTrail_id)
                        .collect(Collectors.toList()))
                        .observe(getViewLifecycleOwner(),trails ->{
                            if(trails!=null && metrics.size()==trails.size()){
                                loadRecyclerView(view, metrics,trails);
                            }
                        } );
            }
        });
        return view;
    }

    private void loadRecyclerView(View view, List<TrailMetrics> metrics,List<Trail> trails){
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            TrailMetricsRecyclerViewAdapter adapter = new TrailMetricsRecyclerViewAdapter(metrics,trails);
            recyclerView.setAdapter(adapter);
            // Set the item click listener
            // Handle the item click event
            adapter.setClickListener(this::replaceFragment);
        }
    }

    private void replaceFragment(TrailMetrics trailMetrics) {
        TrailMetricsDescriptionFragment fragment = TrailMetricsDescriptionFragment.newInstance(trailMetrics.getMetricId());
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.replaceFragment(fragment);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

}
