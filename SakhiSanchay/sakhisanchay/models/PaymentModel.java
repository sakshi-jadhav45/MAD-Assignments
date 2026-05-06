package  com.android.sakhisanchay.models;
public class PaymentModel {
    private String paymentId;
    private double amount;
    private double interest;
    private String date;
    private String type; // e.g., "Loan EMI" or "Bachat Contribution"

    public PaymentModel() {} // Required for Firebase

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PaymentModel(String amount, double interest, String date, String type) {
        this.amount =Double.parseDouble(amount) ;
        this.interest = interest;
        this.date = date;
        this.type = type;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public double getInterest() {
        return interest;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
// Add Getters and Setters here
}