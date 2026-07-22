package main.java.tubes.enums;

public enum OrderStatus {
    PAID, FINISHED;

    public static OrderStatus fromDbValue(String value) {
        return OrderStatus.valueOf(value.toUpperCase());
    }

    public String toDbValue() {
        return this.name().toLowerCase();
    }
}
