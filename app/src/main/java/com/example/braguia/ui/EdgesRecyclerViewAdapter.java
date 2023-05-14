package com.example.braguia.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;
import com.example.braguia.model.trails.Edge;

import java.util.List;

public class EdgesRecyclerViewAdapter extends RecyclerView.Adapter<EdgesRecyclerViewAdapter.ViewHolder> {

    private final List<Edge> mValues;
    private OnItemClickListener listener;

    public EdgesRecyclerViewAdapter(List<Edge> items) {
        mValues = items;
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
        holder.edge_time.setText(String.valueOf(mValues.get(position).getEdge_duration()) + " minutes");
        holder.edge_road_type.setText(mValues.get(position).getEdge_desc());
        //TODO: falta associar os pins (origem e destino) aos cards que ainda nao tem nada neste outer_card

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
        public final CardView edge_origem; //TODO: remove?
        public final CardView edge_destino; //TODO: remove?



        public Edge mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            edge_origem = view.findViewById(R.id.card_pin_view_origem); //TODO: remove?
            edge_destino = view.findViewById(R.id.card_pin_view_destino); //TODO: remove?
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
