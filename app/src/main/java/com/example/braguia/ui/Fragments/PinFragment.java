package com.example.braguia.ui.Fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.braguia.R;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.viewAdapters.EdgeTipViewAdapter;
import com.example.braguia.viewmodel.TrailViewModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PinFragment extends Fragment {
    private int id;
    MediaPlayer mediaPlayer;

    // Factory method to create a new instance of the fragment
    public static PinFragment newInstance(int id) {
        PinFragment fragment = new PinFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id = getArguments().getInt("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(!MapsFragment.meetsPreRequisites(getContext())){
            Toast toast = Toast.makeText(getContext(), "Install Google Maps to navigate", Toast.LENGTH_SHORT);
            toast.show();
        }

        View view = inflater.inflate(R.layout.fragment_pin, container, false);

        TrailViewModel trailViewModel = new ViewModelProvider(requireActivity()).get(TrailViewModel.class);
        trailViewModel.getPinsById(List.of(id)).observe(getViewLifecycleOwner(), x ->{
            if(x!=null && x.size()>0){
                loadView(view, x.get(0));
            }
        });
        return view;
    }

    private void loadView(View view, EdgeTip edgeTip){
        TextView titulo = view.findViewById(R.id.pin_title);
        titulo.setText(edgeTip.getPin_name());

        ImageView imagem = view.findViewById(R.id.pin_img);
        EdgeTipViewAdapter.setImageView(edgeTip,imagem);

        VideoView video = view.findViewById(R.id.pin_vid);
        Log.e("Debug","boolean="+EdgeTipViewAdapter.setVideoView(edgeTip,video));

        Button playButton = view.findViewById(R.id.audio_playbutton);
        mediaPlayer = new MediaPlayer();
        if(edgeTip.hasAudio()){
            playButton.setVisibility(View.VISIBLE);
            EdgeTipViewAdapter.setMediaPlayer(edgeTip,view,mediaPlayer);

            AtomicBoolean isPlaying= new AtomicBoolean(false);
            playButton.setOnClickListener(v -> {
                if(isPlaying.get()){
                    mediaPlayer.pause();
                    isPlaying.set(false);
                } else {
                    mediaPlayer.start();
                    isPlaying.set(true);
                }
            });

        }else {
            playButton.setVisibility(View.GONE);
        }


        // Add touch listener to the video view


        TextView description = view.findViewById(R.id.pin_description);
        description.setText(edgeTip.getPin_desc());

        FragmentManager childFragmentManager = getChildFragmentManager();
        MapsFragment mapsFragment = MapsFragment.newInstance(List.of(edgeTip));
        FragmentTransaction transaction2 = childFragmentManager.beginTransaction();
        transaction2.replace(R.id.pin_mapView, mapsFragment);
        transaction2.commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

