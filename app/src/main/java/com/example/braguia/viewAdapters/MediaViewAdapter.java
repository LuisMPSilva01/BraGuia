package com.example.braguia.viewAdapters;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.braguia.model.trails.Medium;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.Objects;

public class MediaViewAdapter {
    public static boolean setImageView(Medium medium,ImageView view){
        if (medium.getMedia_type().equals("I")){
            Picasso.get().load(medium.getMedia_file().replace("http", "https")).into(view);
            return true;
        } else return false;
    }

    public static boolean setVideoView(Medium medium,VideoView view){
        if (medium.getMedia_type().equals("V")){
            ((VideoView) view).setVideoPath(medium.getMedia_file().replace("http", "https"));
            view.seekTo(1);
            view.setOnTouchListener(new View.OnTouchListener() {
                boolean isPaused = true;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (isPaused) {
                            // Video is currently paused, start playback
                            view.start();
                            isPaused = false;
                        } else {
                            // Video is currently playing, pause playback
                            view.pause();
                            isPaused = true;
                        }
                    }
                    return true;
                }
            });
            return true;
        } else {
            view.setOnTouchListener((view1, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(view1.getContext(), "Não existe um video disponível", Toast.LENGTH_SHORT).show();
                }
                return true;
            });

            return false;
        }
    }

    public static boolean setMediaPlayer(Medium medium,MediaPlayer mediaPlayer) {
        if(Objects.equals(medium.getMedia_type(), "R")){
            try {
                mediaPlayer.setDataSource(medium.getMedia_file().replace("http", "https"));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                Log.e("MediaViewAdapter","Erro com media");
                return false;
            }
            return true;
        } else return false;
    }
}
