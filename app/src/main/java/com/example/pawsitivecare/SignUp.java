package com.example.pawsitivecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText username, no_hp, password, email, address;
    Button SignUp, resendCode;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    ProgressBar progress_bar;
    TextView signin;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username);
        no_hp = findViewById(R.id.no_hp);
        address = findViewById(R.id.address);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        SignUp = findViewById(R.id.SignUp);
        signin = findViewById(R.id.singup);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        progress_bar = findViewById(R.id.progress_bar);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Username = username.getText().toString();
                String Address = address.getText().toString();
                String Number = no_hp.getText().toString();

                if(TextUtils.isEmpty(Email)){
                    email.setError("Email Is Required. ");
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    password.setError("Password is Required. ");
                    return;
                }
                if(password.length()<8){
                    password.setError("Password Must be more than 8 Character");
                    return;
                }
                progress_bar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //sent verification link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignUp.this, "Verification Email Has been sent.", Toast.LENGTH_SHORT).show();
                                }
                            }). addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "OnFailure: Email not Sent" + e.getMessage());
                                }
                            });


                            Toast.makeText(SignUp.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection(("users")).document(userID);
                            Map<String, Object> user = new HashMap<>();

                            user.put("username", Username);
                            user.put("no_hp", Number);
                            user.put("address", Address);
                            user.put("email", Email);
                            user.put("password", Password);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User profile is created for " + userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), VerificationEmail.class));
                        } else {
                            Toast.makeText(SignUp.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progress_bar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}