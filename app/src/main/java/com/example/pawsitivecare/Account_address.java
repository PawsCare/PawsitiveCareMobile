package com.example.pawsitivecare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Account_address extends AppCompatActivity {

    ImageView back_button;
    EditText edit_address;
    Button save;

    // Deklarasi variabel Firestore
    private FirebaseFirestore db;
    private DocumentReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_address);

        back_button = findViewById(R.id.back_button);
        edit_address = findViewById(R.id.edit_address);
        save = findViewById(R.id.save_button);

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance();

        // Mendapatkan referensi pengguna yang sedang login
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            userRef = db.collection("users").document(uid);
        }

        // Mendapatkan data alamat dari Firestore saat aktivitas dibuat
        if (userRef != null) {
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String currentAddress = documentSnapshot.getString("address");
                        edit_address.setText(currentAddress);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Tangani jika gagal mendapatkan data dari Firestore
                    Toast.makeText(Account_address.this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Memberikan listener pada tombol kembali
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Memberikan listener pada tombol simpan
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan alamat baru dari EditText
                String newAddress = edit_address.getText().toString();

                // Memperbarui alamat pengguna di Firestore
                if (userRef != null) {
                    userRef.update("address", newAddress)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Tanggapi jika berhasil menyimpan alamat baru
                                    Toast.makeText(Account_address.this, "Alamat berhasil disimpan", Toast.LENGTH_SHORT).show();
                                    sendNotificationToRealtimeDatabase(); // Kirim notifikasi setelah berhasil memperbarui alamat
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Tanggapi jika gagal menyimpan alamat baru
                                    Toast.makeText(Account_address.this, "Gagal menyimpan alamat", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                finish();
            }
        });

        // Menambahkan listener untuk mendengarkan perubahan pada dokumen pengguna
        if (userRef != null) {
            userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        // Tangani jika terjadi kesalahan saat mendapatkan snapshot
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Ambil alamat terbaru dari Firestore
                        String currentAddress = documentSnapshot.getString("address");
                        // Perbarui UI dengan alamat terbaru
                        edit_address.setText(currentAddress);
                    }
                }
            });
        }
    }

    // Mengirim notifikasi ke Realtime Database setelah alamat berhasil diubah
    private void sendNotificationToRealtimeDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Mendapatkan referensi ke Realtime Database
            DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifikasi").child(user.getUid());

            // Membuat data notifikasi
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("tipe_notifikasi", "Alamat berhasil diubah !");
            notificationData.put("date", dateTime);

            // Mengunggah data notifikasi ke Realtime Database
            notificationsRef.push().setValue(notificationData)
                    .addOnSuccessListener(aVoid -> {
                        // Notifikasi berhasil diunggah
                    })
                    .addOnFailureListener(e -> {
                        // Gagal mengunggah notifikasi
                        Toast.makeText(Account_address.this, "Gagal mengunggah notifikasi", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
