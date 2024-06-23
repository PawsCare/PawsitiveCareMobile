package com.example.pawsitivecare;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private static final String TAG = "CartAdapter";
    private Context context;
    private List<CartItem> cartItemList;
    private DatabaseReference databaseReference;
    private Cart cartActivity;

    public CartAdapter(Context context, List<CartItem> cartItemList, DatabaseReference databaseReference) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.databaseReference = databaseReference;
        if (context instanceof Cart) {
            this.cartActivity = (Cart) context;
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (position < 0 || position >= cartItemList.size()) {
            Log.e(TAG, "Invalid position: " + position);
            return;
        }

        CartItem cartItem = cartItemList.get(position);

        if (cartItem == null) {
            Log.e(TAG, "CartItem is null at position: " + position);
            return;
        }

        // Set data to views
        holder.nama.setText(cartItem.getNama());
        holder.harga.setText(cartItem.getHarga());
        holder.kuantitas.setText(String.valueOf(cartItem.getKuantitas()));

        // Load image using Glide
        Glide.with(context).load(cartItem.getImage()).into(holder.image);

        // Handle actions like increase/decrease quantity or remove item if needed
        holder.kurang.setOnClickListener(v -> {
            Log.d(TAG, "kurang button clicked");
            int quantity = cartItem.getKuantitas();
            if (quantity > 1) {
                cartItem.setKuantitas(quantity - 1);
                holder.kuantitas.setText(String.valueOf(cartItem.getKuantitas()));  // Update TextView
                updateCartItem(cartItem);  // Update Firebase
            } else {
                removeCartItem(cartItem);  // Remove item if quantity reaches 0
            }
        });

        holder.tambah.setOnClickListener(v -> {
            Log.d(TAG, "tambah button clicked");
            int quantity = cartItem.getKuantitas();
            cartItem.setKuantitas(quantity + 1);
            holder.kuantitas.setText(String.valueOf(cartItem.getKuantitas()));  // Update TextView
            updateCartItem(cartItem);  // Update Firebase
        });

        holder.trash.setOnClickListener(v -> {
            Log.d(TAG, "trash button clicked");
            removeCartItem(cartItem);
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView image, kurang, tambah, trash;
        TextView nama, harga, kuantitas;

        public CartViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item);
            nama = itemView.findViewById(R.id.nama);
            harga = itemView.findViewById(R.id.harga);
            kuantitas = itemView.findViewById(R.id.kuantitas);
            kurang = itemView.findViewById(R.id.kurang);
            tambah = itemView.findViewById(R.id.tambah);
            trash = itemView.findViewById(R.id.trash);
        }
    }

    private void updateCartItem(CartItem cartItem) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userCartRef = databaseReference.child(userId).child(cartItem.getKey());
            userCartRef.setValue(cartItem).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (cartActivity != null) {
                        cartActivity.calculateTotal();  // Update total when item quantity changes
                    }
                } else {
                    Toast.makeText(context, "Failed to update item: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void removeCartItem(CartItem cartItem) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userCartRef = databaseReference.child(userId).child(cartItem.getKey());
            userCartRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    cartItemList.remove(cartItem);
                    notifyDataSetChanged();
                    if (cartActivity != null) {
                        cartActivity.calculateTotal();  // Update total when item is removed
                    }
                } else {
                    Toast.makeText(context, "Failed to remove item: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
