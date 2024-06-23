package com.example.pawsitivecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {
    private List<DeliveryModel> deliveryList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(DeliveryModel delivery);
    }

    public DeliveryAdapter(List<DeliveryModel> deliveryList, OnItemClickListener onItemClickListener) {
        this.deliveryList = deliveryList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        DeliveryModel delivery = deliveryList.get(position);
        holder.bind(delivery, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        public TextView estimasi;

        public DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            estimasi = itemView.findViewById(R.id.estimasi);
        }

        public void bind(final DeliveryModel delivery, final OnItemClickListener listener) {
            estimasi.setText(delivery.getEstimasi());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(delivery);
                }
            });
        }
    }
}

