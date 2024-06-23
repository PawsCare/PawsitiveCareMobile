package com.example.pawsitivecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    ImageView buttonBack;
    FirebaseAuth mAuth;
    Button button;
    EditText email;
    ProgressBar progressBar;

    String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        buttonBack = findViewById(R.id.backIcon);
        email = findViewById(R.id.email);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.GONE);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = email.getText().toString().trim();
                if (!TextUtils.isEmpty(strEmail)) {
                    // Menampilkan progress bar dan menonaktifkan tombol
                    progressBar.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                    ResetPassword();
                } else {
                    email.setError("Email field can't be empty");
                }
            }
        });
    }

    private void ResetPassword(){
        mAuth.sendPasswordResetEmail(strEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ForgotPassword.this, "Reset Password link has been sent", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPassword.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        FirebaseAuth.getInstance().signOut();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPassword.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Menyembunyikan progress bar dan mengaktifkan kembali tombol
                        progressBar.setVisibility(View.GONE);
                        button.setEnabled(true);
                    }
                });
    }
}
