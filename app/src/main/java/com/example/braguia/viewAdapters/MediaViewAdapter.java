package com.example.braguia.viewAdapters;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

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

import androidx.test.core.app.ApplicationProvider;

import com.example.braguia.model.trails.Medium;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class MediaViewAdapter {


    public static boolean setImageView(Medium medium,ImageView view)  {
        Context context = view.getContext().getApplicationContext();
        if (medium.getMedia_type().equals("I")){
            // Get stored file
            String filename = medium.getMedia_file().replace("http","").replace("//","").replace("/","");
            File file = new File(context.getFilesDir(), filename);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            view.setImageBitmap(bitmap);
            return true;
        } else return false;
    }

    public static boolean setVideoView(Medium medium,VideoView view){
        Context context = view.getContext().getApplicationContext();
        if (medium.getMedia_type().equals("V")){
            // Get stored file
            String filename = medium.getMedia_file().replace("http","").replace("//","").replace("/","");
            File file = new File(context.getFilesDir(), filename);
            view.seekTo(1);
            //Set video
            view.setVideoPath(file.getAbsolutePath());

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
                    Toast.makeText(view1.getContext(), "No video available", Toast.LENGTH_SHORT).show();
                }
                return true;
            });

            return false;
        }
    }

    public static boolean setMediaPlayer(Medium medium,MediaPlayer mediaPlayer) {
        Context context = getApplicationContext();
        if(Objects.equals(medium.getMedia_type(), "R")){
            try {
                // Get stored file
                String filename = medium.getMedia_file().replace("http","").replace("//","").replace("/","");
                Log.d("oi",filename);
                File file = new File(context.getFilesDir(), filename);
                mediaPlayer.setDataSource(file.getAbsolutePath());
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                Log.e("MediaViewAdapter","Erro com media");
                return false;
            }
            return true;
        } else return false;
    }
}
