package main.java.tubes.model;

import main.java.tubes.enums.*;

public class Paket extends Menu {
    public Paket(int id, String name, double price, String subCategory, String imageUrl) {
        super(id, name, price, MenuType.PAKET, subCategory, imageUrl);
    }
}