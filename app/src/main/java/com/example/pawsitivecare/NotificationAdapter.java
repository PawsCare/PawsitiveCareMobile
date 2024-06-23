package com.example.pawsitivecare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationModel> notificationList;

    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        private TextView notificationTypeTextView;
        private TextView notificationDateTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTypeTextView = itemView.findViewById(R.id.tipe_notifikasi);
            notificationDateTextView = itemView.findViewById(R.id.date_text_view);
        }

        public void bind(NotificationModel notification) {
            notificationTypeTextView.setText(notification.getTipeNotifikasi());
            notificationDateTextView.setText(notification.getDate());
        }
    }
}

