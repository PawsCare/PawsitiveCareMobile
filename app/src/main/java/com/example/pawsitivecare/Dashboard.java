package com.example.pawsitivecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Dashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    PorfileFragment profileFragment = new PorfileFragment();

    // Menambahkan variabel untuk menyimpan ID fragmen saat ini
    private int currentFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState != null) {
            // Mengambil ID fragmen yang disimpan saat instance state disimpan sebelumnya
            currentFragmentId = savedInstanceState.getInt("currentFragment", R.id.home);
        } else {
            // Jika tidak ada instance state sebelumnya, menampilkan fragmen beranda
            currentFragmentId = R.id.home;
        }

        // Menampilkan fragmen yang sesuai saat aktivitas dibuat atau dibuat kembali
        switchFragment(currentFragmentId);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    switchFragment(R.id.home);
                } else if (itemId == R.id.history) {
                    switchFragment(R.id.history);
                } else if (itemId == R.id.notification) {
                    switchFragment(R.id.notification);
                }
                return true;
            }
        });
    }

    // Metode untuk beralih ke fragmen yang sesuai berdasarkan ID
    private void switchFragment(int fragmentId) {
        Fragment fragment;
        if (fragmentId == R.id.home) {
            fragment = homeFragment;
        } else if (fragmentId == R.id.history) {
            fragment = historyFragment;
        } else if (fragmentId == R.id.notification) {
            fragment = notificationFragment;
        } else {
            fragment = homeFragment; // Fragmen beranda menjadi nilai default
        }

        // Mengganti fragmen yang ditampilkan dengan yang baru
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        // Menyimpan ID fragmen saat ini
        currentFragmentId = fragmentId;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Menyimpan ID fragmen saat ini saat instance state disimpan
        outState.putInt("currentFragment", currentFragmentId);
    }
}
