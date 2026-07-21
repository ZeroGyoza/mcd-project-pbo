package main.java.tubes.model;

import main.java.tubes.enums.*;

public class Drink extends Menu {
    public Drink(int id, String name, double price, String subCategory, String imageUrl) {
        super(id, name, price, MenuType.DRINK, subCategory, imageUrl);
    }
}