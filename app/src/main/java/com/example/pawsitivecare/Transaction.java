package com.example.pawsitivecare;

public class Transaction {
    private String alamat;
    private String date;
    private String email;
    private String metode_pembayaran;
    private String no_hp;
    private long total; // atau int
    private String transaction_code;
    private String id_user;
    private String username;

    // Constructor tanpa parameter diperlukan untuk Firebase
    public Transaction() {
    }

    // Constructor dengan parameter
    public Transaction(String alamat, String date, String email, String metode_pembayaran, String no_hp, long total, String transaction_code, String id_user, String username) {
        this.alamat = alamat;
        this.date = date;
        this.email = email;
        this.metode_pembayaran = metode_pembayaran;
        this.no_hp = no_hp;
        this.total = total;
        this.transaction_code = transaction_code;
        this.id_user = id_user;
        this.username = username;
    }

    // Getter dan Setter
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMetode_pembayaran() {
        return metode_pembayaran;
    }

    public void setMetode_pembayaran(String metode_pembayaran) {
        this.metode_pembayaran = metode_pembayaran;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

