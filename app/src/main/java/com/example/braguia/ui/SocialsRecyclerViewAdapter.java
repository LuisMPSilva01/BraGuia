package com.example.braguia.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.app.Social;
import com.example.braguia.ui.Activitys.MainActivity;

import java.util.List;

public class SocialsRecyclerViewAdapter extends RecyclerView.Adapter<SocialsRecyclerViewAdapter.ViewHolder> {

    private final List<Social> mValues;
    public SocialsRecyclerViewAdapter(List<Social> items) {
        mValues = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_socials_item, parent, false);
        return new SocialsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SocialsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.social_name.setText(mValues.get(position).getSocialName());
        holder.social_url.setText(mValues.get(position).getSocialUrl());

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
        CardView cd = holder.mView.findViewById(R.id.socials_card_view);
        cd.setCardBackgroundColor(cardColor);
        holder.social_name.setTextColor(textColor);
        holder.social_url.setTextColor(textColor);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView social_name;
        public final TextView social_url;
        public Social mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            social_name = view.findViewById(R.id.social_name);
            social_url = view.findViewById(R.id.facebook);
        }

        @Override
        public String toString() {
            return super.toString() + social_name+social_url;
        }
    }
}

