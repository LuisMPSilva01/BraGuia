package com.example.braguia.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.viewmodel.TrailViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PinListFragment extends Fragment {
    private TrailViewModel trailViewModel;
    private static final String ARG_MY_LIST = "my_list";

    private List<Integer> ids;



    public static PinListFragment newInstance(List<Integer> ids) {
        PinListFragment fragment = new PinListFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_MY_LIST, new ArrayList<>(ids));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the arguments from the Bundle
        if (getArguments() != null) {
            ids = getArguments().getIntegerArrayList(ARG_MY_LIST);
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

