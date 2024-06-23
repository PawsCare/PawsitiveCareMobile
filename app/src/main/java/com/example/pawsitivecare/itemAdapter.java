package com.example.pawsitivecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolder> {

    Context context;

    List<ItemModel> itemModelList;

    public itemAdapter(Context context, List<ItemModel> itemModelList) {
        this.context = context;
        this.itemModelList = itemModelList;
    }

    itemAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(itemAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nama.setText(itemModelList.get(position).getNama());
        holder.harga.setText(itemModelList.get(position).getHarga());
        Glide.with(context).load(itemModelList.get(position).getImageURL()).into(holder.image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, harga, kategori, deskripsi, merek;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
            harga = itemView.findViewById(R.id.harga);
            image = itemView.findViewById(R.id.imageURL);
            kategori = itemView.findViewById(R.id.kategori);
            deskripsi = itemView.findViewById(R.id.deskripsi);
            merek = itemView.findViewById(R.id.merek);
        }
    }
}
