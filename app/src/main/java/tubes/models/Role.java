package main.java.tubes.models;

public enum Role {
    CUSTOMER, MANAGER, CASHIER;

    public static Role fromDbValue(String value) {
        return Role.valueOf(value.toUpperCase());
    }

    public String toDbValue() {
        return this.name().toLowerCase();
    }
}