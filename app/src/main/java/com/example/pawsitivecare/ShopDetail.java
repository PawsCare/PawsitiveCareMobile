package com.example.pawsitivecare;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShopDetail extends AppCompatActivity {

    ImageView back_button, image;
    TextView nama, harga, kategori, merek, deskripsi;
    TextView button;

    ItemModel itemModel = null;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pesanan");

        nama = findViewById(R.id.nama);
        harga = findViewById(R.id.harga);
        kategori = findViewById(R.id.kategori);
        merek = findViewById(R.id.merek);
        deskripsi = findViewById(R.id.deskripsi);
        image = findViewById(R.id.image);
        back_button = findViewById(R.id.backbutton);
        button = findViewById(R.id.button);

        itemModel = (ItemModel) getIntent().getSerializableExtra("detail");

        if (itemModel != null) {
            nama.setText(itemModel.getNama());
            harga.setText(itemModel.getHarga());
            kategori.setText(itemModel.getKategori());
            merek.setText(itemModel.getMerek());
            deskripsi.setText(itemModel.getDeskripsi());
            Glide.with(this).load(itemModel.getImageURL()).into(image);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back_button.setOnClickListener(v -> finish());

        button.setOnClickListener(v -> addItemToCart());
    }

    private void addItemToCart() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && itemModel != null) {
            String userId = user.getUid();
            DatabaseReference userCartRef = databaseReference.child(userId);

            userCartRef.orderByChild("nama").equalTo(itemModel.getNama())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(ShopDetail.this, "Item already added", Toast.LENGTH_SHORT).show();
                            } else {
                                // Item doesn't exist, add it to the cart
                                DatabaseReference newItemRef = userCartRef.push();
                                HashMap<String, Object> cartItem = new HashMap<>();
                                cartItem.put("image", itemModel.getImageURL());
                                cartItem.put("nama", itemModel.getNama());
                                cartItem.put("harga", itemModel.getHarga());
                                cartItem.put("kuantitas", 1); // Default quantity is 1

                                newItemRef.setValue(cartItem).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ShopDetail.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ShopDetail.this, "Failed to add item to cart: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(ShopDetail.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "User not logged in or item not available", Toast.LENGTH_SHORT).show();
        }
    }


}
