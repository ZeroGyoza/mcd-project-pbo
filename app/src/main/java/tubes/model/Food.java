package main.java.tubes.model;

import main.java.tubes.enums.*;

public class Food extends Menu {
    public Food(int id, String name, double price, String subCategory, String imageUrl) {
        super(id, name, price, MenuType.FOOD, subCategory, imageUrl);
    }
}