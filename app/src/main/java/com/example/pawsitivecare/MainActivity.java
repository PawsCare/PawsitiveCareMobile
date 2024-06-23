package com.example.pawsitivecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnSignUp;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        fAuth = FirebaseAuth.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLogin();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySignUp();
            }
        });
    }

    public void openActivityLogin(){
        Intent OpenActLogin = new Intent(getApplicationContext(), Login.class);
        startActivity(OpenActLogin);
    }
    public void openActivitySignUp(){
        Intent OpenActSignUp = new Intent(getApplicationContext(), SignUp.class);
        startActivity(OpenActSignUp);
    }
}
