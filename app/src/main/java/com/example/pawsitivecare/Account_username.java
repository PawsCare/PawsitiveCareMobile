package com.example.pawsitivecare;

import androidx.annotation.NonNull;
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

public class Account_username extends AppCompatActivity {
    ImageView back_button;
    EditText username;
    Button save;

    // Deklarasi variabel Firestore
    private FirebaseFirestore db;
    private DocumentReference userRef;
    private String uid; // Untuk menyimpan UID pengguna

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_username);

        back_button = findViewById(R.id.back_button);
        username = findViewById(R.id.username);
        save = findViewById(R.id.save_button);

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance();

        // Mendapatkan referensi pengguna yang sedang login
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            uid = currentUser.getUid();
            userRef = db.collection("users").document(uid);
        }

        // Mendapatkan data username dari Firestore saat aktivitas dibuat
        if (userRef != null) {
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String currentUsername = documentSnapshot.getString("username");
                        username.setText(currentUsername);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Tangani jika gagal mendapatkan data dari Firestore
                    Toast.makeText(Account_username.this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Memberikan listener pada tombol kembali
        back_button.setOnClickListener(v -> finish());

        // Memberikan listener pada tombol simpan
        save.setOnClickListener(v -> {
            // Mendapatkan username baru dari EditText
            String newUsername = username.getText().toString();

            // Memperbarui username pengguna di Firestore
            if (userRef != null) {
                userRef.update("username", newUsername)
                        .addOnSuccessListener(aVoid -> {
                            // Tanggapi jika berhasil menyimpan username baru
                            Toast.makeText(Account_username.this, "Username berhasil disimpan", Toast.LENGTH_SHORT).show();
                            sendNotificationToRealtimeDatabase();
                        })
                        .addOnFailureListener(e -> {
                            // Tanggapi jika gagal menyimpan username baru
                            Toast.makeText(Account_username.this, "Gagal menyimpan username", Toast.LENGTH_SHORT).show();
                        });
            }
            finish();
        });

        // Menambahkan listener untuk mendengarkan perubahan pada dokumen pengguna
        if (userRef != null) {
            userRef.addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    // Tangani jika terjadi kesalahan saat mendapatkan snapshot
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Ambil username terbaru dari Firestore
                    String currentUsername = documentSnapshot.getString("username");
                    // Perbarui UI dengan username terbaru
                    username.setText(currentUsername);
                }
            });
        }
    }

    private void sendNotificationToRealtimeDatabase() {
        // Mendapatkan referensi ke Realtime Database
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifikasi").child(uid);

        // Membuat data notifikasi
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("tipe_notifikasi", "Username Berhasil diubah !");
        notificationData.put("date", dateTime);

        // Mengunggah data notifikasi ke Realtime Database
        notificationsRef.push().setValue(notificationData)
                .addOnSuccessListener(aVoid -> {
                    // Notifikasi berhasil diunggah
                })
                .addOnFailureListener(e -> {
                    // Gagal mengunggah notifikasi
                    Toast.makeText(Account_username.this, "Gagal mengunggah notifikasi", Toast.LENGTH_SHORT).show();
                });
    }
}
