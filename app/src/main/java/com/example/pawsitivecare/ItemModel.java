package com.example.pawsitivecare;

import java.io.Serializable;

public class ItemModel implements Serializable {

    private String harga;
    private String merek;
    private String deskripsi;
    private String kategori;
    private String imageURL;
    private String nama;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        this.merek = merek;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ItemModel(String nama, String harga, String merek, String deskripsi, String kategori, String imageURL) {
        this.nama = nama;
        this.harga = harga;
        this.merek = merek;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.imageURL = imageURL;
    }


    public ItemModel() {
    }
}
