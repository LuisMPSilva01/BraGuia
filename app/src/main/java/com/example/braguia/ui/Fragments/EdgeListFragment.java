package com.example.braguia.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.Edge;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.ui.EdgesRecyclerViewAdapter;
import com.example.braguia.viewmodel.TrailViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EdgeListFragment extends Fragment {
    private TrailViewModel trailViewModel;
    private static final String ARG_TRAIL_LIST = "TRAIL_LIST";
    private static final String ARG_PIN_LIST = "PIN_LIST";
    private String type;
    private List<Integer> ids;



    public static EdgeListFragment newInstanceByTrails(List<Integer> ids) {
        EdgeListFragment fragment = new EdgeListFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_TRAIL_LIST, new ArrayList<>(ids));
        fragment.setArguments(args);

        return fragment;
    }

    public static EdgeListFragment newInstanceByPins(List<Integer> ids) {
        EdgeListFragment fragment = new EdgeListFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PIN_LIST, new ArrayList<>(ids));
        fragment.setArguments(args);

        return fragment;
    }

    //TODO: VER A PARTIR DAQUI
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            List<Integer> idsList = getArguments().getIntegerArrayList(ARG_TRAIL_LIST);
            if(idsList != null) {
                ids = idsList;
                type= "trails";
            } else {
                ids = getArguments().getIntegerArrayList(ARG_PIN_LIST);
                type= "pins";
                if(ids == null) {
                    throw new IllegalArgumentException("Both trail ID list and pin ID list are null");
                }
            }
        } else {
            throw new IllegalArgumentException("Arguments bundle is null");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edge_list, container, false);

        trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        if(Objects.equals(type, "trails")){
            trailViewModel.getTrailsById(ids).observe(getViewLifecycleOwner(), trails -> {
                List<Edge> edges = trails.stream()
                        .map(Trail::getEdges)
                        .flatMap(List::stream)
                        .distinct()
                        .collect(Collectors.toList());
                loadView(view, edges);
            });
        }
        return view;
    }

    private void loadView(View view, List<Edge> edges){
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);

            EdgesRecyclerViewAdapter adapter = new EdgesRecyclerViewAdapter(edges);
            recyclerView.setAdapter(adapter);


            //adapter.setOnItemClickListener(this::replaceFragment);
        }
    }


}
