package com.example.braguia.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.app.AppInfo;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.ui.Activitys.MainActivity;
import com.example.braguia.ui.TrailsHomeRecyclerViewAdapter;
import com.example.braguia.viewmodel.AppInfoViewModel;
import com.example.braguia.viewmodel.TrailViewModel;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private AppInfoViewModel appInfoViewModel;
    private TrailViewModel trailsViewModel;

    private TrailsHomeRecyclerViewAdapter adapter;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        trailsViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        try {
            trailsViewModel.getAllTrails().observe(getViewLifecycleOwner(), y -> {
                loadRecyclerView(view, y);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        appInfoViewModel = new ViewModelProvider(requireActivity()).get(AppInfoViewModel.class);
        try {
            appInfoViewModel.getAppInfo().observe(getViewLifecycleOwner(), x -> {
                loadView(view, x);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    private void loadView(View view, AppInfo appInfo ){
        TextView title = view.findViewById(R.id.textView2);
        title.setText(appInfo.getAppDesc());
        TextView intro = view.findViewById(R.id.appIntro);
        intro.setText(appInfo.getAppLandingPageText());

    }

    private void loadRecyclerView(View view, List<Trail> trails){
        RecyclerView recyclerView = view.findViewById(R.id.trail_main_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new TrailsHomeRecyclerViewAdapter(trails);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(this::replaceFragment);


    }

    private void replaceFragment(Trail trail) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", trail.getId());

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navHostFragment.getNavController().navigate(R.id.trailDescriptionFragment,bundle);
    }
}