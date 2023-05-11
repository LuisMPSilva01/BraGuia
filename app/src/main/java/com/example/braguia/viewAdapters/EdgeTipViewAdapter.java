package com.example.braguia.viewAdapters;

import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.braguia.viewAdapters.MediaViewAdapter;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Medium;

import java.io.IOException;

public class EdgeTipViewAdapter{
    public static boolean setImageView(EdgeTip edgeTip,ImageView view){
        for(Medium medium:edgeTip.getMedia()){
            if(MediaViewAdapter.setImageView(medium,view)){
                return true;
            }
        }
        return false;
    }
    public static boolean setVideoView(EdgeTip edgeTip,VideoView view){
        for(Medium medium:edgeTip.getMedia()){
            if(MediaViewAdapter.setVideoView(medium,view)){
                return true;
            }
        }
        return false;
    }
    public static boolean setMediaPlayer(EdgeTip edgeTip, MediaPlayer mediaPlayer){
        for(Medium medium:edgeTip.getMedia()){
            if(MediaViewAdapter.setMediaPlayer(medium,mediaPlayer)){
                return true;
            }
        }
        return false;
    }
}
