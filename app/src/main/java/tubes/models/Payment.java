package main.java.tubes.model;

public class Payment {
    private String method;
    private double amount;
    private boolean isSuccess;

    public Payment(String method, double amount) {
        this.method = method;
        this.amount = amount;
        this.isSuccess = false;
    }

    public String getMethod() { return method; }
    public double getAmount() { return amount; }
    public boolean isSuccess() { return isSuccess; }
    public void setSuccess(boolean success) { isSuccess = success; }
}