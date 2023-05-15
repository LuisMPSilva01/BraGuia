package com.example.braguia.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.Edge;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.ui.Activitys.MainActivity;
import com.example.braguia.viewAdapters.EdgeTipViewAdapter;

import java.util.List;

public class EdgesRecyclerViewAdapter extends RecyclerView.Adapter<EdgesRecyclerViewAdapter.ViewHolder> {

    private final List<Edge> mValues;
    private final Context c;
    private OnItemClickListener listener;

    public EdgesRecyclerViewAdapter(List<Edge> items, Context c) {
        mValues = items;
        this.c = c;
    }

    public interface OnItemClickListener {
        void onItemClick(Edge ed);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_edge_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.edge_transport.setText(mValues.get(position).getEdge_transport());
        holder.edge_time.setText(mValues.get(position).getEdge_duration() + " minutes");
        holder.edge_road_type.setText(mValues.get(position).getEdge_desc());
        holder.pin_name_origem.setText(mValues.get(position).getEdge_start().getPin_name());
        if(!EdgeTipViewAdapter.setImageView(mValues.get(position).getEdge_start(),holder.imageView2_origem)){
            holder.imageView2_origem.setImageResource(R.drawable.no_preview_image);
        }

        holder.pin_name_destino.setText(mValues.get(position).getEdge_end().getPin_name());
        if(!EdgeTipViewAdapter.setImageView(mValues.get(position).getEdge_end(),holder.imageView2_destino)){
            holder.imageView2_destino.setImageResource(R.drawable.no_preview_image);
        }

        // Set text color based on theme mode
        int textColor;
        MainActivity mainActivity = (MainActivity) holder.itemView.getContext();
        if (!mainActivity.isDarkModeEnabled()) {
            textColor = Color.WHITE; // Cor do texto no modo escuro
        } else {
            textColor = Color.BLACK; // Cor do texto no modo claro
        }
        holder.edge_transport.setTextColor(textColor);
        holder.edge_time.setTextColor(textColor);
        holder.edge_road_type.setTextColor(textColor);
        holder.pin_name_origem.setTextColor(textColor);
        holder.pin_name_destino.setTextColor(textColor);

        holder.card_origem.setOnClickListener(e->{
            replaceFragment(mValues.get(position).getEdge_start());
        });

        holder.card_destino.setOnClickListener(e->{
            replaceFragment(mValues.get(position).getEdge_end() );
        });
    }

    private void replaceFragment(EdgeTip ed) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", ed.getId());

        NavHostFragment navHostFragment = (NavHostFragment) ((FragmentActivity) this.c).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navHostFragment.getNavController().navigate(R.id.pinFragment,bundle);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView edge_transport;
        public final TextView edge_time;
        public final TextView edge_road_type;
        public final TextView pin_name_origem;
        public final TextView pin_name_destino;
        public final ImageView imageView2_origem;
        public final ImageView imageView2_destino;

        public final CardView card_origem;
        public final CardView card_destino;



        public Edge mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            card_origem = view.findViewById(R.id.card_pin_view_origem);
            card_destino = view.findViewById(R.id.card_pin_view_destino);
            pin_name_origem = view.findViewById(R.id.pin_name_origem);
            imageView2_origem = view.findViewById(R.id.imageView2_origem);
            pin_name_destino = view.findViewById(R.id.pin_name_destino);
            imageView2_destino = view.findViewById(R.id.imageView2_destino);
            edge_transport = view.findViewById(R.id.edge_transport);
            edge_time = view.findViewById(R.id.edge_time);
            edge_road_type = view.findViewById(R.id.edge_road_type);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
