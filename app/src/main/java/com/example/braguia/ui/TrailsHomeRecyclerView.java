package com.example.braguia.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.Trail;

public class TrailsHomeRecyclerView extends RecyclerView.ViewHolder{
    public final View mView;
    public final TextView trailName;
    public final ImageView imageView;

    public Trail mItem;

    public TrailsHomeRecyclerView(View view) {
        super(view);
        mView = view;
        trailName = view.findViewById(R.id.trail_main_name);
        imageView = view.findViewById(R.id.trail_main_image);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + trailName;
    }

}