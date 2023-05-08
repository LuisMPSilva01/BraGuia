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
import com.example.braguia.model.app.Social;
import com.example.braguia.viewmodel.AppInfoViewModel;

import java.io.IOException;
import java.util.List;

public class SocialsListFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;

    private AppInfoViewModel appInfoViewModel;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SocialsListFragment() {
    }

    public static SocialsListFragment newInstance(int columnCount) {
        SocialsListFragment fragment = new SocialsListFragment();
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
        View view = inflater.inflate(R.layout.fragment_socials_list, container, false);

        appInfoViewModel = new ViewModelProvider(requireActivity()).get(AppInfoViewModel.class);
        try {
            appInfoViewModel.getAppInfo().observe(getViewLifecycleOwner(), appInfo -> {
                List<Social> socials = appInfo.getSocials();
                Log.e("Socials List","socials size:" + socials.size());
                loadRecyclerView(view, socials);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    private void loadRecyclerView(View view, List<Social> socials){
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            SocialsRecyclerViewAdapter adapter = new SocialsRecyclerViewAdapter(socials);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
