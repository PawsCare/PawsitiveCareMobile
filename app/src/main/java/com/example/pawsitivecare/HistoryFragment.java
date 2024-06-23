package com.example.pawsitivecare;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private List<Transaction> transactionList;
    private ImageView historyEmpty;
    private TextView textEmpty;
    private TextView clearHistory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recycleview);
        historyEmpty = view.findViewById(R.id.history_empty);
        textEmpty = view.findViewById(R.id.text_empty);
        clearHistory = view.findViewById(R.id.clear_item);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(getContext(), transactionList, this::showPopup);
        recyclerView.setAdapter(historyAdapter);

        clearHistory.setOnClickListener(v -> clearTransactionHistory());

        fetchTransactions();
        return view;
    }

    private void fetchTransactions() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("transaksi").child(userId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    transactionList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Transaction transaction = snapshot.getValue(Transaction.class);
                        if (transaction != null) {
                            transactionList.add(transaction);
                        }
                    }
                    // Mengurutkan transaksi berdasarkan tanggal, dengan yang terbaru di atas
                    Collections.sort(transactionList, new Comparator<Transaction>() {
                        @Override
                        public int compare(Transaction t1, Transaction t2) {
                            // Assuming date is in the format "yyyy-MM-dd HH:mm:ss"
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            try {
                                Date date1 = sdf.parse(t1.getDate());
                                Date date2 = sdf.parse(t2.getDate());
                                // Reverse order: latest date first
                                return date2.compareTo(date1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });

                    // Menentukan visibilitas elemen berdasarkan apakah daftar kosong atau tidak
                    if (transactionList.isEmpty()) {
                        historyEmpty.setVisibility(View.VISIBLE);
                        textEmpty.setVisibility(View.VISIBLE);
                        clearHistory.setVisibility(View.GONE);
                    } else {
                        historyEmpty.setVisibility(View.GONE);
                        textEmpty.setVisibility(View.GONE);
                        clearHistory.setVisibility(View.VISIBLE);
                    }

                    historyAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });
        }
    }

    private void showPopup(Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.popup_history, null);

        TextView dateTextView = view.findViewById(R.id.date);
        TextView transactionCodeTextView = view.findViewById(R.id.transaction_code);
        TextView usernameTextView = view.findViewById(R.id.username);
        TextView emailTextView = view.findViewById(R.id.email);
        TextView noHpTextView = view.findViewById(R.id.no_hp);
        TextView addressTextView = view.findViewById(R.id.address);
        TextView metodePembayaranTextView = view.findViewById(R.id.metode_pembayaran);
        TextView totalTextView = view.findViewById(R.id.total);

        dateTextView.setText(transaction.getDate());
        transactionCodeTextView.setText(transaction.getTransaction_code());
        usernameTextView.setText(transaction.getUsername());
        emailTextView.setText(transaction.getEmail());
        noHpTextView.setText(transaction.getNo_hp());
        addressTextView.setText(transaction.getAlamat());
        metodePembayaranTextView.setText(transaction.getMetode_pembayaran());
        totalTextView.setText(String.valueOf(transaction.getTotal()));

        builder.setView(view);
        AlertDialog dialog = builder.create();

        TextView finishButton = view.findViewById(R.id.finish_button);
        finishButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void clearTransactionHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Konfirmasi");
        builder.setMessage("Ingin Menghapus semua History?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("transaksi").child(userId);
                    databaseReference.removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            transactionList.clear();
                            historyAdapter.notifyDataSetChanged();
                            historyEmpty.setVisibility(View.VISIBLE);
                            textEmpty.setVisibility(View.VISIBLE);
                            clearHistory.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getContext(), "Gagal menghapus riwayat", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
