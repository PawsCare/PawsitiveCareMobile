package com.example.pawsitivecare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Cart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView totalTextView, checkoutButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private ImageView backbutton;
    private TextView addressTextView, noHpTextView;
    private ImageView emptyImageView;
    private TextView emptyTextView;

    private static final String TAG = "CartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recycler_view_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItemList, FirebaseDatabase.getInstance().getReference().child("pesanan"));
        recyclerView.setAdapter(cartAdapter);

        totalTextView = findViewById(R.id.total);
        checkoutButton = findViewById(R.id.checkout_button);
        backbutton = findViewById(R.id.backbutton);
        emptyImageView = findViewById(R.id.empty_image);
        emptyTextView = findViewById(R.id.empty_text);

        // Tambahkan referensi untuk TextView address dan no_hp
        addressTextView = findViewById(R.id.address);
        noHpTextView = findViewById(R.id.no_hp);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            loadCartItems(user.getUid());
            loadUserDetails(user.getUid(), addressTextView, noHpTextView); // Panggil metode baru untuk memuat data pengguna
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        checkoutButton.setOnClickListener(v -> processCheckout());
        updateCheckoutButtonState(0); // Set initial state
    }

    private void loadCartItems(String userId) {
        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference().child("pesanan").child(userId);
        userCartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartItemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItem.setKey(snapshot.getKey());
                        cartItemList.add(cartItem);
                    }
                }
                cartAdapter.notifyDataSetChanged();
                int total = calculateTotal();
                updateCheckoutButtonState(total); // Update button state based on total

                if (cartItemList.isEmpty()) {
                    emptyImageView.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.VISIBLE);
                } else {
                    emptyImageView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public int calculateTotal() {
        int total = 0;
        for (CartItem cartItem : cartItemList) {
            int price = Integer.parseInt(cartItem.getHarga());
            total += price * cartItem.getKuantitas();
        }
        totalTextView.setText(String.valueOf(total)); // Set the total to the TextView
        return total; // Return the calculated total
    }

    private void updateCheckoutButtonState(int total) {
        if (total == 0) {
            checkoutButton.setBackgroundResource(R.drawable.button_abu);
            checkoutButton.setEnabled(false);
        } else {
            checkoutButton.setBackgroundResource(R.drawable.button_white);
            checkoutButton.setEnabled(true);
        }
    }

    private void loadUserDetails(String userId, TextView addressTextView, TextView noHpTextView) {
        DocumentReference userRef = firestore.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String address = documentSnapshot.getString("address");
                String noHp = documentSnapshot.getString("no_hp");

                if (address != null) {
                    addressTextView.setText(address);
                } else {
                    addressTextView.setText("Alamat tidak tersedia");
                }

                if (noHp != null) {
                    noHpTextView.setText(noHp);
                } else {
                    noHpTextView.setText("Nomor HP tidak tersedia");
                }
            } else {
                Toast.makeText(Cart.this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(Cart.this, "Failed to retrieve user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void processCheckout() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String email = user.getEmail();

            // Retrieve user details from Firestore
            DocumentReference userRef = firestore.collection("users").document(userId);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("username");
                    String noHp = documentSnapshot.getString("no_hp");

                    if (username != null && noHp != null) {
                        int total = calculateTotal(); // Calculate total price
                        int totalQuantity = calculateTotalQuantity(); // Calculate total quantity
                        int shippingCost = 12000; // Static shipping cost
                        int discount = 7000; // Static discount
                        int grandTotal = total + shippingCost - discount; // Calculate grand total

                        // Create an Intent to start CheckOut activity
                        Intent intent = new Intent(Cart.this, CheckOut.class);
                        intent.putExtra("total_quantity", totalQuantity);
                        intent.putExtra("total_price", total);
                        intent.putExtra("shipping_cost", shippingCost);
                        intent.putExtra("discount", discount);
                        intent.putExtra("grand_total", grandTotal);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Cart.this, "Failed to get user details", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Cart.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(Cart.this, "Failed to retrieve user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private int calculateTotalQuantity() {
        int totalQuantity = 0;
        for (CartItem cartItem : cartItemList) {
            totalQuantity += cartItem.getKuantitas();
        }
        return totalQuantity;
    }

    private void uploadTransaction(String userId, String username, String email, String noHp, int total) {
        String transactionCode = generateTransactionCode();
        String paymentMethod = "BCA";

        DatabaseReference transactionRef = FirebaseDatabase.getInstance().getReference().child("transaksi").child(transactionCode);

        // Get current date and time
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Prepare transaction data
        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("transaction_code", transactionCode);
        transactionData.put("user_id", userId);
        transactionData.put("username", username);
        transactionData.put("email", email);
        transactionData.put("no_hp", noHp);
        transactionData.put("total", total);
        transactionData.put("metode_pembayaran", paymentMethod);
        transactionData.put("date", currentDateTime); // Add date field

        // Upload transaction data to Realtime Database
        transactionRef.setValue(transactionData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Cart.this, "Transaction successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Cart.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String generateTransactionCode() {
        int length = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder transactionCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            transactionCode.append(characters.charAt(index));
        }
        return transactionCode.toString();
    }
}
