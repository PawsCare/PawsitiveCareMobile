package com.example.pawsitivecare;

public class StatusPaket {
    private String estimasi;

    public StatusPaket() {
        // Default constructor required for calls to DataSnapshot.getValue(StatusPaket.class)
    }

    public String getEstimasi() {
        return estimasi;
    }

    public void setEstimasi(String estimasi) {
        this.estimasi = estimasi;
    }
}

