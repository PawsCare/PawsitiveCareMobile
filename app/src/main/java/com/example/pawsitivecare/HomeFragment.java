package com.example.pawsitivecare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    ImageView cart, pawmart, buatjanji, donasi, promo1, promo2, chatdokter, statusPaket;
    CircleImageView profile_image;
    TextView Username;
    Button button_rabies, button_distemper, button_luka, button_kutu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();

        pawmart = view.findViewById(R.id.pawmart);
        cart = view.findViewById(R.id.maps);
        buatjanji = view.findViewById(R.id.buat_janji);
        donasi = view.findViewById(R.id.donasi);
        button_rabies = view.findViewById(R.id.button_rabies);
        button_distemper = view.findViewById(R.id.button_stemper);
        Username = view.findViewById(R.id.username);
        profile_image = view.findViewById(R.id.profile_image);
        promo1 = view.findViewById(R.id.promo_1);
        promo2 = view.findViewById(R.id.promo_2);
        chatdokter = view.findViewById(R.id.chat_dokter);
        statusPaket = view.findViewById(R.id.status_paket);
        button_kutu = view.findViewById(R.id.button_kutu);
        button_luka = view.findViewById(R.id.button_luka);

        if (user != null) {
            DocumentReference documentReference = fStore.collection("users").document(user.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String username = document.getString("username");
                            Username.setText(username);

                            String userID = fAuth.getCurrentUser().getUid();
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();

                            StorageReference profileRef = storageRef.child("users/" + userID + "/profile.jpg");

                            profileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Picasso.get().load(task.getResult()).into(profile_image);
                                    } else {
                                    }
                                }
                            });
                        }
                    } else {
                    }
                }
            });
        }

        statusPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Delivery.class);
                startActivity(intent);
            }
        });

        chatdokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatDokter.class);
                startActivity(intent);
            }
        });

        button_rabies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), penyakit_rabies.class);
                startActivity(intent);
            }
        });

        button_luka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Luka.class);
                startActivity(intent);
            }
        });

        button_kutu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Kutu.class);
                startActivity(intent);
            }
        });

        button_distemper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), distemper.class);
                startActivity(intent);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Cart.class);
                startActivity(intent);
            }
        });

        pawmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopNew.class);
                startActivity(intent);
            }
        });

        promo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopNew.class);
                startActivity(intent);
            }
        });

        promo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopNew.class);
                startActivity(intent);
            }
        });

        buatjanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), janjiKunjungan.class);
                startActivity(intent);
            }
        });

        donasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.pawsitivecare.donasi.class);
                startActivity(intent);
            }
        });

        Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Profile.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
