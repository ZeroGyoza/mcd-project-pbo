package main.java.tubes.models;

public class OrderItem {
    private int id;
    private int orderId;
    private String menuName;
    private double price;
    private int quantity;

    public OrderItem() {}

    public OrderItem(int id, int orderId, String menuName, double price, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubtotal() { return price * quantity; }
}
