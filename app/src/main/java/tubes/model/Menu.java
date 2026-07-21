package main.java.tubes.model;

import main.java.tubes.enums.*;

public abstract class Menu {
    private int id;
    private String name;
    private double price;
    private MenuType type;
    private String subCategory;
    private String imageUrl;

    public Menu(int id, String name, double price, MenuType type, String subCategory, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.subCategory = subCategory;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public MenuType getType() {
        return type;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}