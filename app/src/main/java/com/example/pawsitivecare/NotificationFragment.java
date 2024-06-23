package com.example.pawsitivecare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList;
    private DatabaseReference notificationsRef;
    private TextView clearItemTextView, textEmpty;
    private ImageView notificationEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        clearItemTextView = view.findViewById(R.id.clear_item);
        textEmpty = view.findViewById(R.id.text_empty);
        notificationEmpty = view.findViewById(R.id.notification_empty);

        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notificationsRef = FirebaseDatabase.getInstance().getReference("notifikasi").child(userId);

        notificationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String tipeNotifikasi = dataSnapshot.child("tipe_notifikasi").getValue(String.class);
                    String date = dataSnapshot.child("date").getValue(String.class);

                    NotificationModel notification = new NotificationModel(tipeNotifikasi, date);
                    notificationList.add(notification);
                }
                Collections.sort(notificationList); // Sort in descending order by date
                adapter.notifyDataSetChanged();
                toggleEmptyView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error here
            }
        });

        clearItemTextView.setOnClickListener(v -> showConfirmationDialog());

        return view;
    }

    private void toggleEmptyView() {
        if (notificationList.isEmpty()) {
            clearItemTextView.setVisibility(View.GONE);
            notificationEmpty.setVisibility(View.VISIBLE);
            textEmpty.setVisibility(View.VISIBLE);
        } else {
            clearItemTextView.setVisibility(View.VISIBLE);
            notificationEmpty.setVisibility(View.GONE);
            textEmpty.setVisibility(View.GONE);
        }
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah ingin menghapus notifikasi?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearNotifications();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void clearNotifications() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notificationsRef.removeValue((error, ref) -> {
            if (error == null) {
                notificationList.clear();
                adapter.notifyDataSetChanged();
                toggleEmptyView();
            } else {
                // Handle error here
            }
        });
    }
}
