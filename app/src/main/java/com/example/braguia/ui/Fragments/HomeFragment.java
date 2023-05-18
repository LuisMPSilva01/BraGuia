package com.example.braguia.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.app.AppInfo;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.model.user.User;
import com.example.braguia.ui.viewAdapters.TrailsHomeRecyclerViewAdapter;
import com.example.braguia.viewmodel.AppInfoViewModel;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
        UserViewModel userViewModel= new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        try {
            trailsViewModel.getAllTrails().observe(getViewLifecycleOwner(), y -> {
                try {
                    userViewModel.getUser().observe(getViewLifecycleOwner(), user ->{
                        if(user!=null){
                            loadRecyclerView(view, y,user);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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

    private void loadRecyclerView(View view, List<Trail> trails, User user){
        RecyclerView recyclerView = view.findViewById(R.id.trail_main_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        Toast.makeText(getContext(),"Setting up",Toast.LENGTH_LONG).show();
        adapter = new TrailsHomeRecyclerViewAdapter(trails);
        recyclerView.setAdapter(adapter);

        if(Objects.equals(user.getUser_type(), "Premium") || Objects.equals(user.getUser_type(), "premium")){
            adapter.setOnItemClickListener(this::replaceFragment);
        } else {
            adapter.setOnItemClickListener(e->{
            });
        }
    }

    private void replaceFragment(Trail trail) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", trail.getId());

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navHostFragment.getNavController().navigate(R.id.trailDescriptionFragment,bundle);
    }
}