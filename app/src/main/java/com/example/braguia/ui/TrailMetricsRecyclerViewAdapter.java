package com.example.braguia.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.app.Partner;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.repositories.TrailRepository;
import com.example.braguia.viewmodel.TrailViewModel;
import com.example.braguia.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrailMetricsRecyclerViewAdapter extends RecyclerView.Adapter<TrailMetricsRecyclerViewAdapter.ViewHolder> {

    private final List<TrailMetrics> metrics;
    private final List<Trail> trails;
    private ClickListener listener;

    public TrailMetricsRecyclerViewAdapter(List<TrailMetrics> metrics,List<Trail> trailItems) {
        this.metrics=metrics;
        this.trails=trailItems;
    }
    public interface ClickListener {
        void onItemClick(TrailMetrics metrics);
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trail_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = trails.get(position);
        holder.trailName.setText(trails.get(position).getTrail_name());
        String durationText = trails.get(position).getTrail_duration() + " minutos";
        holder.duration.setText(durationText);
        holder.difficulty.setText(trails.get(position).getTrail_difficulty());
        Picasso.get().load(trails.get(position)
                        .getTrail_img().replace("http", "https"))
                .into(holder.imageView);

        // Set click listener for each item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(metrics.get(position));
            }

        });
    }


    @Override
    public int getItemCount() {
        return trails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView trailName;
        public final TextView difficulty;
        public final TextView duration;
        public final ImageView imageView;
        public Trail mItem;

        public ViewHolder(View view) {
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
}