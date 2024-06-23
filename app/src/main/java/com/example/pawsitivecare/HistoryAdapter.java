package com.example.pawsitivecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<Transaction> transactionList;
    private OnItemClickListener onItemClickListener;

    public HistoryAdapter(Context context, List<Transaction> transactionList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.transactionList = transactionList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.dateTextView.setText(transaction.getDate());
        holder.totalTextView.setText(String.valueOf(transaction.getTotal()));
        // Set other fields if necessary

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(transaction));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView totalTextView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            totalTextView = itemView.findViewById(R.id.total);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }
}
