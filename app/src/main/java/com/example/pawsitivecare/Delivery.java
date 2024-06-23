package com.example.pawsitivecare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Delivery extends AppCompatActivity {

    private ImageView backbutton;
    private RecyclerView recyclerView;
    private DeliveryAdapter adapter;
    private List<DeliveryModel> deliveryList;
    private DatabaseReference deliveriesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        backbutton = findViewById(R.id.backbutton);

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        deliveryList = new ArrayList<>();
        adapter = new DeliveryAdapter(deliveryList, new DeliveryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DeliveryModel delivery) {
                showPopup(delivery);
            }
        });
        recyclerView.setAdapter(adapter);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        deliveriesRef = FirebaseDatabase.getInstance().getReference("status_paket").child(userId);

        deliveriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deliveryList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String id = dataSnapshot.getKey();
                    String estimasi = dataSnapshot.child("estimasi").getValue(String.class);
                    String noResi = dataSnapshot.child("no_resi").getValue(String.class);
                    String transactionCode = dataSnapshot.child("transaction_code").getValue(String.class);

                    DeliveryModel delivery = new DeliveryModel(id, estimasi, noResi, transactionCode);
                    deliveryList.add(delivery);
                }
                adapter.notifyDataSetChanged();
                toggleEmptyView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error here
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void toggleEmptyView() {
        if (deliveryList.isEmpty()) {
            findViewById(R.id.text_empty_delivery).setVisibility(View.VISIBLE);
            findViewById(R.id.deliery_empty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.text_empty_delivery).setVisibility(View.GONE);
            findViewById(R.id.deliery_empty).setVisibility(View.GONE);
        }
    }
    private void showPopup(final DeliveryModel delivery) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_delivery, null);
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setView(popupView)
                .create();

        final TextView noResiTextView = popupView.findViewById(R.id.no_resi);
        final TextView usernameTextView = popupView.findViewById(R.id.username);
        final TextView addressTextView = popupView.findViewById(R.id.address);
        final TextView transactionCodeTextView = popupView.findViewById(R.id.transaction_code);

        noResiTextView.setText(delivery.getNoResi());
        transactionCodeTextView.setText(delivery.getTransactionCode());

        // Fetch user info from Firestore
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");
                            String address = documentSnapshot.getString("address");

                            usernameTextView.setText(username);
                            addressTextView.setText(address);
                        }
                    }
                });

        popupView.findViewById(R.id.belum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        popupView.findViewById(R.id.terima_paket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the item from the list
                deliveryList.remove(delivery);
                adapter.notifyDataSetChanged();
                toggleEmptyView();

                // Remove the item from the database
                deliveriesRef.child(delivery.getId()).removeValue();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}



