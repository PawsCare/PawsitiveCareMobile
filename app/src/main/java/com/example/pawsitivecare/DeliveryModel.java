package com.example.pawsitivecare;

public class DeliveryModel {
    private String id;
    private String estimasi;
    private String noResi;
    private String transactionCode;

    public DeliveryModel() {
        // Default constructor required for calls to DataSnapshot.getValue(DeliveryModel.class)
    }

    public DeliveryModel(String id, String estimasi, String noResi, String transactionCode) {
        this.id = id;
        this.estimasi = estimasi;
        this.noResi = noResi;
        this.transactionCode = transactionCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstimasi() {
        return estimasi;
    }

    public void setEstimasi(String estimasi) {
        this.estimasi = estimasi;
    }

    public String getNoResi() {
        return noResi;
    }

    public void setNoResi(String noResi) {
        this.noResi = noResi;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
}



