public class Transaction {
    private String date;
    private String transactionCode;
    private int total;

    public Transaction() {
        // Default constructor required for calls to DataSnapshot.getValue(Transaction.class)
    }

    public Transaction(String date, String transactionCode, int total) {
        this.date = date;
        this.transactionCode = transactionCode;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public int getTotal() {
        return total;
    }
}
