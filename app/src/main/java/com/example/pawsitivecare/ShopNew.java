package com.example.pawsitivecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopNew extends AppCompatActivity {

    RecyclerView recyclerView;
    itemAdapter itemAdapter;
    ImageView backbutton, cart;

    List<ItemModel> itemModelList;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_new);

        recyclerView = findViewById(R.id.pop_shop);
        db = FirebaseFirestore.getInstance();
        backbutton = findViewById(R.id.back_button);
        cart = findViewById(R.id.cart);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        itemModelList = new ArrayList<>();
        itemAdapter = new itemAdapter(this, itemModelList);
        recyclerView.setAdapter(itemAdapter);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopNew.this, Cart.class);
                startActivity(intent);
            }
        });

        db.collection("shop_item").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        ItemModel itemModel = document.toObject(ItemModel.class);
                        itemModelList.add(itemModel);
                        itemAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(ShopNew.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        itemAdapter.setOnItemClickListener(new itemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ShopNew.this, ShopDetail.class);
                intent.putExtra("detail", itemModelList.get(position));
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
