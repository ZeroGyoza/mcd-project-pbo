package main.java.tubes.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private double totalAmount;
    private String paymentMethod;
    private String status;
    private List<CartItem> items;

    public Order() {
        this.items = new ArrayList<>();
        this.status = "PENDING";
    }

    public Order(double totalAmount, List<CartItem> items) {
        this.totalAmount = totalAmount;
        this.items = items;
        this.status = "PENDING";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}