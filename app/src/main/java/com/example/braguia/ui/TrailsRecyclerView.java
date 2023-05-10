package com.example.braguia.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.Trail;

public class TrailsRecyclerView extends RecyclerView.ViewHolder{
    public final View mView;
    public final TextView trailName;
    public final TextView difficulty;
    public final TextView duration;
    public final ImageView imageView;
    public Trail mItem;

    public TrailsRecyclerView(View view) {
        super(view);
        mView = view;
        trailName = view.findViewById(R.id.trailTitle);
        difficulty = view.findViewById(R.id.trailDifficulty);
        duration = view.findViewById(R.id.trailTime);
        imageView = view.findViewById(R.id.cardimage);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + trailName+difficulty+duration;
    }

}