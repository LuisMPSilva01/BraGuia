package com.example.braguia.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.Trail;
import com.example.braguia.ui.Activitys.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrailsRecyclerViewAdapter extends RecyclerView.Adapter<TrailsRecyclerView> {

    private final List<Trail> originalData;

    private List<Trail> filteredData;

    private OnItemClickListener listener;

    public TrailsRecyclerViewAdapter(List<Trail> items) {
        originalData = items;
        filteredData = new ArrayList<>(items);
    }

    public interface OnItemClickListener {
        void onItemClick(Trail trail);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public void filterData(String query) {
        filteredData.clear();
        for (Trail data : originalData) {
            if (data.getTrail_name().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                filteredData.add(data);
            }
        }
        notifyDataSetChanged();
    }

    public void reset(){
        filteredData = new ArrayList<>(originalData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailsRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trail_item, parent, false);

        return new TrailsRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(final TrailsRecyclerView holder, int position) {
        holder.mItem = filteredData.get(position);
        holder.trailName.setText(filteredData.get(position).getTrail_name());
        String durationText = filteredData.get(position).getTrail_duration() + " minutes";
        holder.duration.setText(durationText);
        holder.difficulty.setText(filteredData.get(position).getTrail_difficulty());
        Picasso.get().load(filteredData.get(position)
                .getTrail_img().replace("http", "https"))
                .into(holder.imageView);

        // Set text color based on theme mode
        int textColor;
        MainActivity mainActivity = (MainActivity) holder.itemView.getContext();
        if (mainActivity.isDarkModeEnabled()) {
            textColor = Color.WHITE;
        } else {
            textColor = Color.BLACK;
        }
        holder.trailName.setTextColor(textColor);
        holder.duration.setTextColor(textColor);
        holder.difficulty.setTextColor(textColor);

        // Set click listener for each item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(originalData.get(position));
            }

        });
    }

    @Override
    public int getItemCount() {return filteredData.size();}


}
