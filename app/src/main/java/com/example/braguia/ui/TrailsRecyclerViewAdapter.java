package com.example.braguia.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.Trail;
import com.squareup.picasso.Picasso;
import java.util.List;

public class TrailsRecyclerViewAdapter extends RecyclerView.Adapter<TrailsRecyclerViewAdapter.ViewHolder> {

    private final List<Trail> mValues;

    public TrailsRecyclerViewAdapter(List<Trail> items) {
        mValues = items;
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
        holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
        Picasso.get().load(mValues.get(position)
                .getTrail_img().replace("http", "https"))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final ImageView imageView;
        public Trail mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            imageView = view.findViewById(R.id.cardimage);
        }

        @Override
        public String toString() {
            return super.toString() + mIdView;
        }
    }
}
