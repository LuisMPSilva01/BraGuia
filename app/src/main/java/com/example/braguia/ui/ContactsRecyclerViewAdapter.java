package com.example.braguia.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.app.Contact;

import java.util.List;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> {

    private final List<Contact> mValues;
    private OnPhoneClickListener listener;
    public ContactsRecyclerViewAdapter(List<Contact> items) {
        mValues = items;
    }

    public void setOnPhoneClickListener(OnPhoneClickListener listener) {
        this.listener = listener;
    }

    public interface OnPhoneClickListener {
        void onPhoneClick(String phoneNumber);
    }

    @Override
    public ContactsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contact_item, parent, false);
        return new ContactsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.contact_name.setText(mValues.get(position).getContactName());
        holder.contact_phone.setText(mValues.get(position).getContactPhone());
        holder.contact_mail.setText(mValues.get(position).getContactMail());
        holder.contact_url.setText(mValues.get(position).getContactUrl());

        // Set click listener for phone
        holder.contact_phone.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhoneClick(mValues.get(position).getContactPhone());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView contact_name;
        public final TextView contact_phone;
        public final TextView contact_url;
        public final TextView contact_mail;
        public Contact mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            contact_name = view.findViewById(R.id.contact_name);
            contact_phone = view.findViewById(R.id.contact_phone);
            contact_url = view.findViewById(R.id.contact_url);
            contact_mail = view.findViewById(R.id.contact_mail);
        }

        @Override
        public String toString() {
            return super.toString() + contact_name+contact_phone+contact_url+contact_mail;
        }
    }
}

