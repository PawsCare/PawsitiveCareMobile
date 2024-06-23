package com.example.pawsitivecare;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class rawat_cat extends AppCompatActivity {

    ImageView hubWA, bb, lokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rawat_cat);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bb = findViewById(R.id.backbutton);
        hubWA = findViewById(R.id.button_layanan);
        lokasi = findViewById(R.id.lokasi_beepet);

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        hubWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://wa.me/+6287752166613";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ganti URL di bawah ini dengan URL Google Maps yang Anda inginkan
                String mapsUrl = "https://maps.app.goo.gl/ugjrFRYxqRikeHB27"; // Contoh koordinat untuk Jakarta
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mapsUrl));
                startActivity(intent);
            }
        });
    }
}
