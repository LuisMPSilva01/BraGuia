package com.example.braguia.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.viewmodel.TrailViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/*
* Neste momento, este fragmento só imprime o conteudo escrito no centro do ecrã.
* TODO: Podemos permitir pesquisa por nome, sugerir locais (random?) debaixo da searchview, ...
* */
public class DiscoverFragment extends Fragment {

    private SearchView searchView;


    private TrailsRecyclerView recyclerView;
    private TrailsRecyclerViewAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_discover, container, false);

        View view = rootView.findViewById(R.id.trail_recycle_view);

        // Load trails data
        TrailViewModel trailsViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        try {
            trailsViewModel.getAllTrails().observe(getViewLifecycleOwner(), x -> {
                Log.e("Trailist","trails size:" + x.size());
                loadRecyclerView(view, x);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        searchView = rootView.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query==null || query.equals("")){
                    adapter.reset();
                }else{
                    adapter.filterData(query);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText==null || newText.equals("")){
                    adapter.reset();
                }else{
                    adapter.filterData(newText);
                }
                return true;
            }
        });

        return rootView;
    }

    private void loadRecyclerView(View view, List<Trail> trails){
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            int mColumnCount = 1;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            adapter = new TrailsRecyclerViewAdapter(trails);
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