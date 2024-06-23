package com.example.pawsitivecare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckOut extends AppCompatActivity {

    private static final String TAG = "CheckOutActivity";

    private TextView grandTotalTextView2, totalQuantityTextView, totalTextView, shippingCostTextView, discountTextView, grandTotalTextView, bayarTextView;
    private LinearLayout selectedPaymentMethod = null;
    private ImageView back_button;
    private String selectedPaymentMethodText = "";
    private DatabaseReference transaksiRef;
    private FirebaseFirestore firestore;
    private String userId;
    private AlertDialog alertDialog;
    private ProgressBar progressBar;
    private TextView bayarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        totalQuantityTextView = findViewById(R.id.total_kuantitas);
        totalTextView = findViewById(R.id.total);
        shippingCostTextView = findViewById(R.id.ongkir);
        discountTextView = findViewById(R.id.diskon);
        grandTotalTextView = findViewById(R.id.grand_total);
        grandTotalTextView2 = findViewById(R.id.grandtotal);
        bayarTextView = findViewById(R.id.bayar);
        back_button = findViewById(R.id.backbutton);
        progressBar = findViewById(R.id.progressBar);
        bayarButton = findViewById(R.id.bayar);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        transaksiRef = firebaseDatabase.getReference("transaksi");
        firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        back_button.setOnClickListener(v -> finish());

        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            Log.e(TAG, "User not logged in");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Intent intent = getIntent();
        int totalQuantity = intent.getIntExtra("total_quantity", 0);
        int totalPrice = intent.getIntExtra("total_price", 0);
        int shippingCost = intent.getIntExtra("shipping_cost", 0);
        int discount = intent.getIntExtra("discount", 0);
        int grandTotal = intent.getIntExtra("grand_total", 0);

        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalTextView.setText(String.valueOf(totalPrice));
        shippingCostTextView.setText(String.valueOf(shippingCost));
        discountTextView.setText(String.valueOf(discount));
        grandTotalTextView.setText(String.valueOf(grandTotal));
        grandTotalTextView2.setText(String.valueOf(grandTotal));

        bayarButton.setOnClickListener(v -> {
            if (selectedPaymentMethod == null) {
                Toast.makeText(CheckOut.this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
            } else {
                bayarButton.setEnabled(false); // Disable the button immediately upon click
                progressBar.setVisibility(View.VISIBLE);
                // Reset cart before saving transaction
                resetCart(totalQuantity, totalPrice, shippingCost, discount, grandTotal);
            }
        });
    }

    public void onPaymentMethodClick(View view) {
        if (selectedPaymentMethod != null) {
            selectedPaymentMethod.setBackground(getDrawable(R.drawable.choose_default));
        }
        selectedPaymentMethod = (LinearLayout) view;
        selectedPaymentMethod.setBackground(getDrawable(R.drawable.choose));

        TextView paymentTextView = (TextView) selectedPaymentMethod.getChildAt(1); // Assuming the TextView is the second child
        selectedPaymentMethodText = paymentTextView.getText().toString();
    }

    private void resetCart(int totalQuantity, int totalPrice, int shippingCost, int discount, int grandTotal) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("pesanan").child(userId);
        cartRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Call fetchUserDataAndSaveTransaction() after resetting the cart
                    fetchUserDataAndSaveTransaction(totalQuantity, totalPrice, shippingCost, discount, grandTotal);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to reset cart", e);
                    progressBar.setVisibility(View.GONE);
                    bayarButton.setEnabled(true); // Re-enable the button upon failure
                    Toast.makeText(CheckOut.this, "Gagal menghapus item dari cart", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchUserDataAndSaveTransaction(int totalQuantity, int totalPrice, int shippingCost, int discount, int grandTotal) {
        firestore.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String email = documentSnapshot.getString("email");
                        String noHp = documentSnapshot.getString("no_hp");
                        String username = documentSnapshot.getString("username");
                        String address = documentSnapshot.getString("address");

                        saveTransactionToFirebase(totalQuantity, totalPrice, shippingCost, discount, grandTotal, email, noHp, username, address);
                    } else {
                        Log.e(TAG, "User data not found for userId: " + userId);
                        progressBar.setVisibility(View.GONE);
                        bayarButton.setEnabled(true); // Re-enable the button if user data is not found
                        Toast.makeText(CheckOut.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to fetch user data", e);
                    progressBar.setVisibility(View.GONE);
                    bayarButton.setEnabled(true); // Re-enable the button upon failure
                    Toast.makeText(CheckOut.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveTransactionToFirebase(int totalQuantity, int totalPrice, int shippingCost, int discount, int grandTotal, String email, String noHp, String username, String address) {
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String transactionCode = "TX" + System.currentTimeMillis();

        // Ubah lokasi referensi database untuk transaksi ke "transaksi/userID"
        DatabaseReference userTransactionRef = transaksiRef.child(userId);

        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("date", dateTime);
        transactionData.put("email", email);
        transactionData.put("metode_pembayaran", selectedPaymentMethodText);
        transactionData.put("no_hp", noHp);
        transactionData.put("total", grandTotal);
        transactionData.put("transaction_code", transactionCode);
        transactionData.put("username", username);
        transactionData.put("alamat", address);

        // Upload data transaksi ke "transaksi/userID"
        userTransactionRef.push().setValue(transactionData)
                .addOnSuccessListener(aVoid -> {
                    // Save notification to Firebase Realtime Database
                    saveNotificationToFirebase(dateTime);

                    // Save status paket to Firebase Realtime Database
                    saveStatusPaketToFirebase(dateTime, transactionCode);

                    progressBar.setVisibility(View.GONE);
                    showTransactionSuccessDialog(dateTime, transactionCode, username, email, address, selectedPaymentMethodText, grandTotal);
                    bayarButton.setEnabled(true); // Re-enable the button upon success
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to save transaction", e);
                    progressBar.setVisibility(View.GONE);
                    bayarButton.setEnabled(true); // Re-enable the button upon failure
                    Toast.makeText(CheckOut.this, "Failed to save transaction", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveStatusPaketToFirebase(String dateTime, String transactionCode) {
        // Calculate estimated delivery date (3 days after transaction date)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        String estimatedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

        // Generate random number for "no_resi"
        String noResi = "RESI" + System.currentTimeMillis();

        DatabaseReference statusPaketRef = FirebaseDatabase.getInstance().getReference("status_paket").child(userId);

        Map<String, Object> statusPaketData = new HashMap<>();
        statusPaketData.put("estimasi", estimatedDate);
        statusPaketData.put("no_resi", noResi);
        statusPaketData.put("transaction_code", transactionCode);

        statusPaketRef.push().setValue(statusPaketData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Status paket saved successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to save status paket", e));
    }

    private void saveNotificationToFirebase(String dateTime) {
        DatabaseReference notifRef = FirebaseDatabase.getInstance().getReference("notifikasi").child(userId);
        String notifKey = notifRef.push().getKey();

        if (notifKey != null) {
            Map<String, Object> notifData = new HashMap<>();
            notifData.put("date", dateTime);
            notifData.put("tipe_notifikasi", "Pesanan Berhasil!");

            notifRef.child(notifKey).setValue(notifData)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Notification saved successfully"))
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to save notification", e));
        }
    }

    private void showTransactionSuccessDialog(String dateTime, String transactionCode, String username, String email, String address, String paymentMethod, int total) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckOut.this);
        View dialogView = getLayoutInflater().inflate(R.layout.popup_transaksi, null);
        builder.setView(dialogView);

        TextView dateTextView = dialogView.findViewById(R.id.date);
        TextView transactionCodeTextView = dialogView.findViewById(R.id.transaction_code);
        TextView usernameTextView = dialogView.findViewById(R.id.username);
        TextView emailTextView = dialogView.findViewById(R.id.email);
        TextView addressTextView = dialogView.findViewById(R.id.address);
        TextView paymentMethodTextView = dialogView.findViewById(R.id.metode_pembayaran);
        TextView totalTextView = dialogView.findViewById(R.id.total);
        TextView finishButton = dialogView.findViewById(R.id.finish_button);

        dateTextView.setText(dateTime);
        transactionCodeTextView.setText(transactionCode);
        usernameTextView.setText(username);
        emailTextView.setText(email);
        addressTextView.setText(address);
        paymentMethodTextView.setText(paymentMethod);
        totalTextView.setText(String.valueOf(total));

        alertDialog = builder.create();
        alertDialog.show();

        finishButton.setOnClickListener(v -> {
            alertDialog.dismiss();
            finish();
        });
    }
}
