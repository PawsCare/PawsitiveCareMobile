package com.example.pawsitivecare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BeepetMakassar extends AppCompatActivity {

    ImageView backbutton, lokasi;
    TextView hubWA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_beepet_makassar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backbutton = findViewById(R.id.backbutton);
        hubWA = findViewById(R.id.hubungiWA);
        lokasi = findViewById(R.id.Map);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        hubWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ganti URL di bawah ini dengan URL yang Anda inginkan
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