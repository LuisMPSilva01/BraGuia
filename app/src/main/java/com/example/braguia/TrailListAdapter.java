package com.example.braguia;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.braguia.objects.Trail;

public class TrailListAdapter extends ListAdapter<Trail, TrailViewHolder> {

    public TrailListAdapter(@NonNull DiffUtil.ItemCallback<Trail> diffCallback) {
        super(diffCallback);
    }

    @Override
    public TrailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TrailViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(TrailViewHolder holder, int position) {
        Trail current = getItem(position);
        holder.bind(current.getTrailImg());
    }

    static class TrailDiff extends DiffUtil.ItemCallback<Trail> {

        @Override
        public boolean areItemsTheSame(@NonNull Trail oldItem, @NonNull Trail newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Trail oldItem, @NonNull Trail newItem) {
            return oldItem.getId() == newItem.getId(); //todo incompleto
        }
    }
}
