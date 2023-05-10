package com.example.braguia.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PinFragment extends Fragment {
    private final EdgeTip edgeTip;
    public PinFragment(EdgeTip edgeTip){
        this.edgeTip=edgeTip;
    }

    public static PinFragment newInstance(EdgeTip edgeTip) {
        return new PinFragment(edgeTip);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(!MapsFragment.meetsPreRequisites(getContext())){
            Toast toast = Toast.makeText(getContext(), "Instale o Google Maps para poder navegar", Toast.LENGTH_SHORT);
            toast.show();
        }

        View view = inflater.inflate(R.layout.fragment_pin, container, false);



        TextView titulo = view.findViewById(R.id.pin_title);
        titulo.setText(edgeTip.getPin_name());

        //ImageView imagem = view.findViewById(R.id.pin_img);
        //Picasso.get().load(tra
        //                .replace("http", "https"))
       //         .into(imagem);

        TextView description = view.findViewById(R.id.pin_description);
        description.setText(edgeTip.getPin_desc());

        FragmentManager childFragmentManager = getChildFragmentManager();
        MapsFragment mapsFragment = MapsFragment.newInstance(List.of(edgeTip));
        FragmentTransaction transaction2 = childFragmentManager.beginTransaction();
        transaction2.replace(R.id.pin_mapView, mapsFragment);
        transaction2.commit();
        return view;
    }
}

