package com.example.pawsitivecare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Account_contact_information extends AppCompatActivity {

    ImageView back_button;
    TextView email;
    EditText number;
    Button save;

    // Deklarasi variabel Firestore
    private FirebaseFirestore db;
    private DocumentReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_contact_information);

        back_button = findViewById(R.id.back_button);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);
        save = findViewById(R.id.save_button);

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance();

        // Mendapatkan referensi pengguna yang sedang login
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            userRef = db.collection("users").document(uid);
        }

        // Mendapatkan data kontak informasi dari Firestore saat aktivitas dibuat
        if (userRef != null) {
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String currentEmail = documentSnapshot.getString("email");
                        String currentNumber = documentSnapshot.getString("no_hp");

                        // Set text untuk email dan nomor telepon
                        email.setText(currentEmail);
                        number.setText(currentNumber);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Tangani jika gagal mendapatkan data dari Firestore
                    Toast.makeText(Account_contact_information.this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
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
                        // Ambil email dan nomor telepon terbaru dari Firestore
                        String currentEmail = documentSnapshot.getString("email");
                        String currentNumber = documentSnapshot.getString("no_hp");

                        // Perbarui UI dengan email dan nomor telepon terbaru
                        email.setText(currentEmail);
                        number.setText(currentNumber);
                    }
                }
            });
        }

        // Menambahkan listener untuk tombol "Save"
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan nomor telepon baru dari EditText
                String newNumber = number.getText().toString();

                // Memperbarui nomor telepon pengguna di Firestore
                if (userRef != null) {
                    userRef.update("no_hp", newNumber)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Tanggapi jika berhasil menyimpan nomor telepon baru
                                    Toast.makeText(Account_contact_information.this, "Nomor telepon berhasil disimpan", Toast.LENGTH_SHORT).show();
                                    sendNotificationToRealtimeDatabase(); // Kirim notifikasi setelah berhasil memperbarui nomor telepon
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Tanggapi jika gagal menyimpan nomor telepon baru
                                    Toast.makeText(Account_contact_information.this, "Gagal menyimpan nomor telepon", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                finish();
            }
        });
    }

    // Mengirim notifikasi ke Realtime Database setelah nomor telepon berhasil diubah
    private void sendNotificationToRealtimeDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Mendapatkan referensi ke Realtime Database
            DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifikasi").child(user.getUid());

            // Membuat data notifikasi
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("tipe_notifikasi", "No. HP berhasil diubah !");
            notificationData.put("date", dateTime);

            // Mengunggah data notifikasi ke Realtime Database
            notificationsRef.push().setValue(notificationData)
                    .addOnSuccessListener(aVoid -> {
                        // Notifikasi berhasil diunggah
                    })
                    .addOnFailureListener(e -> {
                        // Gagal mengunggah notifikasi
                        Toast.makeText(Account_contact_information.this, "Gagal mengunggah notifikasi", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
