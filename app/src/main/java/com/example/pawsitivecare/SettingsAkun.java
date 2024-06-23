package com.example.pawsitivecare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;

public class SettingsAkun extends AppCompatActivity {

    ImageView back_button, username, password, contact_information, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_akun);

        back_button = findViewById(R.id.back_button_account);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        contact_information = findViewById(R.id.contact_information);
        address = findViewById(R.id.address);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menutup aktivitas saat tombol kembali diklik
                finish();
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsAkun.this, Account_username.class);
                startActivity(intent);
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsAkun.this, Account_password.class);
                startActivity(intent);
            }
        });

        contact_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsAkun.this, Account_contact_information.class);
                startActivity(intent);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsAkun.this, Account_address.class);
                startActivity(intent);
            }
        });
    }
}
