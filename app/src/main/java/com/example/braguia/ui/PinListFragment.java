package com.example.braguia.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.viewmodel.TrailViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PinListFragment extends Fragment {
    private TrailViewModel trailViewModel;
    private static final String ARG_TRAIL_LIST = "TRAIL_LIST";
    private static final String ARG_PIN_LIST = "PIN_LIST";

    private List<Integer> ids;



    public static PinListFragment newInstanceByTrails(List<Integer> ids) {
        PinListFragment fragment = new PinListFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_TRAIL_LIST, new ArrayList<>(ids));
        fragment.setArguments(args);

        return fragment;
    }

    public static PinListFragment newInstanceByPins(List<Integer> ids) {
        PinListFragment fragment = new PinListFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PIN_LIST, new ArrayList<>(ids));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            List<Integer> idsList = getArguments().getIntegerArrayList(ARG_TRAIL_LIST);
            if(idsList != null) {
                ids = idsList;
            } else {
                ids = getArguments().getIntegerArrayList(ARG_PIN_LIST);
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
        View view = inflater.inflate(R.layout.fragment_pin_list, container, false);

        trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        trailViewModel.getTrailsById(ids).observe(getViewLifecycleOwner(), x -> loadView(view, x)); //TODO:FIX!
        return view;
    }

    private void loadView(View view, List<Trail> trails){
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);

            List<EdgeTip> edgeTips = trails.stream()
                    .map(e->e.getEdges()
                            .stream()
                            .flatMap(es -> Stream.of(es.getEdge_start(), es.getEdge_end()))
                            .collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .distinct()
                    .collect(Collectors.toList());
            PinsRecyclerViewAdapter adapter = new PinsRecyclerViewAdapter(edgeTips);
            recyclerView.setAdapter(adapter);


            adapter.setOnItemClickListener(this::replaceFragment);
        }
    }

    private void replaceFragment(EdgeTip edgeTip) { //TODO maybe adicionar um backtrace a partir da main activity para tornar o fragmento mais fléxivel
        PinFragment fragment = PinFragment.newInstance(edgeTip);
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.replaceFragment(fragment);
    }
}

