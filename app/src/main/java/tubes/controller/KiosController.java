package main.java.tubes.controller;

import main.java.tubes.model.*;
import main.java.tubes.repositories.MenuRepository;
import java.util.ArrayList;
import java.util.List;

public class KiosController {
    private MenuRepository repo;
    private List<CartItem> cart;

    public KiosController() {
        this.repo = new MenuRepository();
        this.cart = new ArrayList<>();
    }

    public List<Menu> getMenuBySubCategory(String subCategory) {
        List<Menu> all = repo.getAllMenu();
        if (subCategory.equalsIgnoreCase("All"))
            return all;

        List<Menu> filtered = new ArrayList<>();
        for (Menu m : all) {
            if (m.getSubCategory().equalsIgnoreCase(subCategory)) {
                filtered.add(m);
            }
        }
        return filtered;
    }

    public List<CartItem> getCart() {
        return this.cart;
    }

    public void addToCart(Menu menu, int qty) {
        if (qty <= 0)
            return;
        for (CartItem item : cart) {
            if (item.getMenu().getId() == menu.getId()) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }
        cart.add(new CartItem(menu, qty));
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void clearCart() {
        cart.clear();
    }
}