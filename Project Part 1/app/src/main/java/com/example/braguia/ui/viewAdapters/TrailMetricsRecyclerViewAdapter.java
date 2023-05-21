package com.example.braguia.ui.viewAdapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.ui.Activitys.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

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
                .inflate(R.layout.fragment_trail_metrics_item, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = trails.get(position);
        holder.trailName.setText(trails.get(position).getTrail_name());
        String durationText = trails.get(position).getTrail_duration() + " minutes";
        holder.duration.setText(durationText);
        holder.difficulty.setText(trails.get(position).getTrail_difficulty());
        Picasso.get().load(trails.get(position)
                        .getTrail_img().replace("http", "https"))
                .into(holder.imageView);

        holder.timeUsed.setText(metrics.get(position).getTimeTaken() + " seconds");
        holder.percentage.setText(Float.toString(metrics.get(position).getCompletedPercentage()));

        // Set text color based on theme mode
        int textColor, cardColor;
        MainActivity mainActivity = (MainActivity) holder.itemView.getContext();
        if (mainActivity.isDarkModeEnabled()) {
            textColor = Color.WHITE;
            cardColor = Color.GRAY;
        } else {
            textColor = Color.BLACK;
            cardColor = Color.WHITE;
        }
        holder.trailName.setTextColor(textColor);
        holder.duration.setTextColor(textColor);
        holder.difficulty.setTextColor(textColor);
        holder.timeUsed.setTextColor(textColor);
        holder.percentage.setTextColor(textColor);
        CardView cd = holder.mView.findViewById(R.id.card_view);
        cd.setCardBackgroundColor(cardColor);
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

        public final TextView percentage;
        public final TextView timeUsed;


        public Trail mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            trailName = view.findViewById(R.id.trailTitle);
            difficulty = view.findViewById(R.id.trailDifficulty);
            duration = view.findViewById(R.id.trailTime);
            imageView = view.findViewById(R.id.cardimage);
            timeUsed = view.findViewById(R.id.trail_metricsTImeTaken);
            percentage = view.findViewById(R.id.trail_metricsCompletePercentage);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + trailName+difficulty+duration;
        }

    }
}
