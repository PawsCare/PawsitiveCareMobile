package com.example.pawsitivecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class janjiKunjungan extends AppCompatActivity {

    CardView petshop_daengta, petshop_makassar, petshop_amigos;
    ImageView back_button, rawatcat,rawatdog,cekcat,cekdog,vaksincat,vaksindog;
    TextView lihat_semua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_janji_kunjungan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        petshop_daengta = findViewById(R.id.petshop_daengta);
        petshop_makassar = findViewById(R.id.petshop_makassar);
        petshop_amigos = findViewById(R.id.petshop_amigos);
        back_button = findViewById(R.id.backbutton);
        lihat_semua = findViewById(R.id.lihat_semua);

        rawatcat = findViewById(R.id.rawatcat);
        rawatdog = findViewById(R.id.rawatdog);
        cekcat = findViewById(R.id.cekcat);
        cekdog = findViewById(R.id.cekdog);
        vaksincat = findViewById(R.id.vaksincat);
        vaksindog = findViewById(R.id.vaksindog);


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lihat_semua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(janjiKunjungan.this, layanan.class);
                startActivity(intent);
            }
        });
        petshop_daengta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(janjiKunjungan.this, BeepetDaengta.class);
                startActivity(intent);
            }
        });

        petshop_amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(janjiKunjungan.this, BeepetAmigos.class);
                startActivity(intent);
            }
        });
        petshop_makassar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(janjiKunjungan.this, BeepetMakassar.class);
                startActivity(intent);
            }
        });


        rawatcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(janjiKunjungan.this, rawat_cat.class);
                startActivity(intent);
            }
        });

        rawatdog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(janjiKunjungan.this, rawat_dog.class);
                startActivity(intent);
            }
        });

        cekcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(janjiKunjungan.this, cekkucing.class);
                startActivity(intent);
            }
        });

        cekdog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(janjiKunjungan.this, cekanjing.class);
                startActivity(intent);
            }
        });

        vaksincat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(janjiKunjungan.this, vaksincat_purevax.class);
                startActivity(intent);
            }
        });

        vaksindog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(janjiKunjungan.this, vaksin_dog.class);
                startActivity(intent);
            }
        });

    }
}