package com.example.pawsitivecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Account_password extends AppCompatActivity {
    ImageView back_button;
    TextView forgotpw;
    Button save;
    ProgressBar progressBar;
    EditText old_password, new_password, confirm_password;

    // Deklarasi variabel Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_password);

        back_button = findViewById(R.id.back_button);
        old_password = findViewById(R.id.old_password);
        new_password = findViewById(R.id.new_password);
        confirm_password = findViewById(R.id.confirm_password);
        save = findViewById(R.id.save_button);
        progressBar = findViewById(R.id.progress_bar);
        forgotpw = findViewById(R.id.forgotPW);

        // Inisialisasi Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Pastikan progress bar disembunyikan pada awalnya
        progressBar.setVisibility(View.GONE);

        back_button.setOnClickListener(v -> finish());

        save.setOnClickListener(v -> {
            // Mengambil nilai dari EditText
            String oldPassword = old_password.getText().toString().trim();
            final String newPassword = new_password.getText().toString().trim();
            String confirmPassword = confirm_password.getText().toString().trim();

            // Validasi
            if (newPassword.equals(confirmPassword)) {
                // Menampilkan progress bar dan menonaktifkan tombol save
                progressBar.setVisibility(View.VISIBLE);
                save.setEnabled(false);

                // Memperbarui password
                updatePassword(oldPassword, newPassword);
            } else {
                // Password baru dan konfirmasi password tidak cocok
                Toast.makeText(Account_password.this, "Password baru dan konfirmasi password tidak cocok.", Toast.LENGTH_SHORT).show();
            }
        });

        forgotpw.setOnClickListener(v -> {
            Intent intent = new Intent(Account_password.this, ForgotPassword.class);
            startActivity(intent);
            finish();
        });
    }

    // Memperbarui password pengguna di Firebase Authentication dan di dokumen pengguna di Firestore
    private void updatePassword(String oldPassword, final String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Menyiapkan credential dengan password lama
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

            // Re-authenticate pengguna dengan password lama
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Re-authentication berhasil, update password baru di Firebase Authentication
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Password berhasil diperbarui di Firebase Authentication, lanjutkan dengan memperbarui di Firestore
                                                    updatePasswordInFirestore(newPassword);
                                                    sendNotificationToRealtimeDatabase(); // Kirim notifikasi setelah berhasil memperbarui password
                                                } else {
                                                    // Gagal memperbarui password di Firebase Authentication
                                                    Toast.makeText(Account_password.this, "Gagal memperbarui password.", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    save.setEnabled(true);
                                                }
                                            }
                                        });
                            } else {
                                // Re-authentication gagal, password lama salah
                                Toast.makeText(Account_password.this, "Password lama salah.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                save.setEnabled(true);
                            }
                        }
                    });
        }
    }

    // Memperbarui password pengguna di dokumen pengguna di Firestore
    private void updatePasswordInFirestore(final String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Mendapatkan ID dokumen pengguna
            String userId = user.getUid();

            // Mendapatkan referensi ke dokumen pengguna di Firestore
            DocumentReference userRef = mFirestore.collection("users").document(userId);

            // Memperbarui password di dokumen pengguna di Firestore
            userRef.update("password", newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Password berhasil diperbarui di Firestore
                                Toast.makeText(Account_password.this, "Password berhasil diperbarui.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                save.setEnabled(true);
                                finish();
                            } else {
                                // Gagal memperbarui password di Firestore
                                Toast.makeText(Account_password.this, "Gagal memperbarui password di Firestore.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                save.setEnabled(true);
                            }
                        }
                    });
        }
    }
    // Mengirim notifikasi ke Realtime Database setelah password berhasil diubah
    private void sendNotificationToRealtimeDatabase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Mendapatkan referensi ke Realtime Database
            DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifikasi").child(user.getUid());

            // Membuat data notifikasi
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("tipe_notifikasi", "Password Berhasil diubah !");
            notificationData.put("date", dateTime);

            // Mengunggah data notifikasi ke Realtime Database
            notificationsRef.push().setValue(notificationData)
                    .addOnSuccessListener(aVoid -> {
                        // Notifikasi berhasil diunggah
                    })
                    .addOnFailureListener(e -> {
                        // Gagal mengunggah notifikasi
                        Toast.makeText(Account_password.this, "Gagal mengunggah notifikasi", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
