package com.example.braguia.ui.viewAdapters;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.braguia.model.trails.Medium;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

public class MediaViewAdapter {


    public static boolean setImageView(Medium medium,ImageView view)  {
        Context context = view.getContext().getApplicationContext();
        if (medium.getMedia_type().equals("I")){
            // Get stored file
            String filename = medium.getMedia_file().replace("http://","").replace("/","");
            File file = new File(context.getFilesDir(),filename);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                view.setImageBitmap(bitmap);
            }else{
                Picasso.get().load(medium.getMedia_file().replace("http", "https")).into(view);
            }

            return true;
        } else return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    public static boolean setVideoView(Medium medium, VideoView view){
        Context context = view.getContext().getApplicationContext();
        if (medium.getMedia_type().equals("V")){
            // Get stored file
            String filename = medium.getMedia_file().replace("http://","").replace("/","");
            File file = new File(context.getFilesDir(),filename);
            if (file.exists()) {
                view.setVideoPath(file.getAbsolutePath());
            }else{
                ((VideoView) view).setVideoPath(medium.getMedia_file().replace("http", "https"));
            }
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

        } else {
            view.setOnTouchListener((view1, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(view1.getContext(), "No video available", Toast.LENGTH_SHORT).show();
                }
                return true;
            });

            return false;
        }
        return true;
    }

    public static boolean setMediaPlayer(Medium medium,View view,MediaPlayer mediaPlayer) {
        Context context = view.getContext().getApplicationContext();
        if(Objects.equals(medium.getMedia_type(), "R")){
            try {
                String filename = medium.getMedia_file().replace("http://","").replace("/","");
                File file = new File(context.getFilesDir(),filename);
                if (file.exists()) {
                    mediaPlayer.setDataSource(file.getAbsolutePath());
                }else{
                   mediaPlayer.setDataSource(medium.getMedia_file().replace("http", "https"));
                }
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                Log.e("MediaViewAdapter","Erro com media");
                return false;
            }
            return true;
        } else return false;
    }
}
