package com.example.braguia.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.List;

public class TrailHistoryFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;

    private UserViewModel userViewModel;

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

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getTrailHistory(requireActivity()).observe(getViewLifecycleOwner(), x -> {
            Log.e("Trailist","trails size:" + x.size());
            loadRecyclerView(view, x);
        });
        return view;
    }

    private void loadRecyclerView(View view, List<Trail> trails){
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            TrailsRecyclerViewAdapter adapter = new TrailsRecyclerViewAdapter(trails);
            recyclerView.setAdapter(adapter);
            // Set the item click listener
            // Handle the item click event
            adapter.setOnItemClickListener(this::replaceFragment);
        }
    }

    private void replaceFragment(Trail trail) { //TODO maybe adicionar um backtrace a partir da main activity para tornar o fragmento mais fléxivel
        // Create a new instance of the destination fragment
        TrailDescriptionFragment fragment = TrailDescriptionFragment.newInstance(trail.getId());
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.replaceFragment(fragment);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

}
