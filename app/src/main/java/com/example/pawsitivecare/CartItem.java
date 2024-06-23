// CartItem.java
package com.example.pawsitivecare;

public class CartItem {
    private String key;
    private String nama;
    private String harga;
    private String image;
    private int kuantitas;

    // Empty constructor for Firebase
    public CartItem() {}

    // Constructor
    public CartItem(String key, String nama, String harga, String image, int kuantitas) {
        this.key = key;
        this.nama = nama;
        this.harga = harga;
        this.image = image;
        this.kuantitas = kuantitas;
    }

    // Getters and Setters

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }
}

