package com.example.braguia.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.app.Partner;
import com.example.braguia.ui.Activitys.MainActivity;

import java.util.List;

public class PartnersRecyclerViewAdapter extends RecyclerView.Adapter<PartnersRecyclerViewAdapter.ViewHolder> {

    private final List<Partner> mValues;
    private OnPhoneClickListener listener;
    public PartnersRecyclerViewAdapter(List<Partner> items) {
        mValues = items;
    }

    public void setOnPhoneClickListener(OnPhoneClickListener listener) {
        this.listener = listener;
    }

    public interface OnPhoneClickListener {
        void onPhoneClick(String phoneNumber);
    }

    @Override
    public PartnersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_partners_item, parent, false);
        return new PartnersRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PartnersRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.partner_name.setText(mValues.get(position).getPartnerName());
        holder.partner_phone.setText(mValues.get(position).getPartnerPhone());
        holder.partner_mail.setText(mValues.get(position).getPartnerMail());
        holder.partner_url.setText(mValues.get(position).getPartnerUrl());

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
        holder.partner_name.setTextColor(textColor);
        holder.partner_phone.setTextColor(textColor);
        holder.partner_mail.setTextColor(textColor);
        holder.partner_url.setTextColor(textColor);
        CardView cd = holder.mView.findViewById(R.id.partners_card_view);
        cd.setCardBackgroundColor(cardColor);

        // Set click listener for phone
        holder.partner_phone.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhoneClick(mValues.get(position).getPartnerPhone());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView partner_name;
        public final TextView partner_phone;
        public final TextView partner_url;
        public final TextView partner_mail;
        public Partner mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            partner_name = view.findViewById(R.id.partner_name);
            partner_phone = view.findViewById(R.id.partner_phone);
            partner_url = view.findViewById(R.id.partner_url);
            partner_mail = view.findViewById(R.id.partner_mail);
        }

        @Override
        public String toString() {
            return super.toString() + partner_name+partner_phone+partner_url+partner_mail;
        }
    }
}

