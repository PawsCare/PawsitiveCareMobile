package com.example.pawsitivecare;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapter extends FragmentStateAdapter {
    private int countTab;

    public PageAdapter(@NonNull FragmentActivity fragmentActivity, int countTab) {
        super(fragmentActivity);
        this.countTab = countTab;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new informasi_new();
            case 1:
                return new buatjanji_new();
            default:
                return new informasi_new();
        }
    }

    @Override
    public int getItemCount() {
        return countTab;
    }
}
