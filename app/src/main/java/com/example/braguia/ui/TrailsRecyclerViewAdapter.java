package com.example.braguia.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.Trail;
import com.squareup.picasso.Picasso;
import java.util.List;

public class TrailsRecyclerViewAdapter extends RecyclerView.Adapter<TrailsRecyclerViewAdapter.ViewHolder> {

    private final List<Trail> mValues;
    private OnItemClickListener listener;

    public TrailsRecyclerViewAdapter(List<Trail> items) {
        mValues = items;
    }

    public interface OnItemClickListener {
        void onItemClick(Trail trail);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.trailName.setText(mValues.get(position).getTrail_name());
        String durationText = mValues.get(position).getTrail_duration() + " minutos";
        holder.duration.setText(durationText);
        holder.difficulty.setText(mValues.get(position).getTrail_difficulty());
        Picasso.get().load(mValues.get(position)
                .getTrail_img().replace("http", "https"))
                .into(holder.imageView);

        // Set click listener for each item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(mValues.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        @Override
        public String toString() {
            return super.toString() + trailName+difficulty+duration;
        }
    }
}
