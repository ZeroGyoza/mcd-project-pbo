package main.java.tubes.controller;

import main.java.tubes.model.*;
import main.java.tubes.repositories.CartRepository;
import main.java.tubes.repositories.MenuRepository;
import main.java.tubes.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class KiosController {
    private MenuRepository repo;
    private OrderRepository orderRepo;
    private CartRepository cartRepo;
    private List<CartItem> cart;
    private int currentCartId;

    public KiosController() {
        this.repo = new MenuRepository();
        this.orderRepo = new OrderRepository();
        this.cartRepo = new CartRepository();
        this.cart = new ArrayList<>();
        
        // Buat atau ambil ID Cart aktif di PostgreSQL
        this.currentCartId = cartRepo.getOrCreateActiveCartId();
    }

    public List<Menu> getMenuBySubCategory(String subCategory) {
        List<Menu> all = repo.getAllMenu();
        if (subCategory == null || subCategory.equalsIgnoreCase("All")) {
            return all;
        }

        List<Menu> filtered = new ArrayList<>();
        for (Menu m : all) {
            if (m.getSubCategory() != null && m.getSubCategory().equalsIgnoreCase(subCategory)) {
                filtered.add(m);
            }
        }
        return filtered;
    }

    public List<CartItem> getCart() {
        return this.cart;
    }

    public void addToCart(Menu menu, int qty) {
        if (qty <= 0 || menu == null) return;

        // 1. Simpan ke Database PostgreSQL (tabel carts & cart_items)
        cartRepo.addOrUpdateItem(currentCartId, menu.getId(), qty);

        // 2. Simpan ke List Memori (RAM)
        for (CartItem item : cart) {
            if (item.getMenu().getId() == menu.getId()) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }
        cart.add(new CartItem(menu, qty));
    }

    public void updateItemQuantity(CartItem item, int newQuantity) {
        if (item == null) return;

        if (newQuantity <= 0) {
            removeItemFromCart(item);
        } else {
            item.setQuantity(newQuantity);
            // Sync update quantity ke PostgreSQL
            cartRepo.updateQuantity(currentCartId, item.getMenu().getId(), newQuantity);
        }
    }

    public void removeItemFromCart(CartItem item) {
        if (item == null) return;

        cart.remove(item);
        // Sync hapus item di PostgreSQL
        cartRepo.removeItem(currentCartId, item.getMenu().getId());
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cart) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void clearCart() {
        // Clear keranjang di DB & hapus/selesaikan sesi cart
        cartRepo.clearCart(currentCartId);
        
        // Clear di RAM
        cart.clear();
        
        // Buat session cart baru di PostgreSQL untuk transaksi berikutnya
        this.currentCartId = cartRepo.getOrCreateActiveCartId();
    }

    public boolean simpanPesananKeDatabase(Order order) {
        if (order == null) return false;

        // Salin isi keranjang saat ini ke objek Order
        order.setItems(new ArrayList<>(this.cart));
        
        // Simpan ke tabel orders / order_items
        boolean success = orderRepo.saveOrder(order);
        
        if (success) {
            clearCart();
        }
        
        return success;
    }
}