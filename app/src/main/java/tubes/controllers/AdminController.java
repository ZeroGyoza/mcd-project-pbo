package main.java.tubes.controllers;

import main.java.tubes.exceptions.RepositoryException;
import main.java.tubes.models.Category;
import main.java.tubes.models.Menu;
import main.java.tubes.models.Order;
import main.java.tubes.repositories.*;

import java.util.List;

public class AdminController {
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    public AdminController() {
        this.categoryRepository = new CategoryRepositoryImpl();
        this.orderRepository = new OrderRepositoryImpl();
        this.menuRepository = new MenuRepository();
    }

    // ---- Category ----
    public List<Category> getAllCategories() throws RepositoryException {
        return categoryRepository.findAll();
    }

    public void addCategory(String name) throws RepositoryException {
        categoryRepository.save(new Category(0, name));
    }

    public void deleteCategory(int id) throws RepositoryException {
        categoryRepository.deleteById(id);
    }

    // ---- Menu ----
    public List<Menu> getAllMenu() {
        return menuRepository.getAllMenu();
    }

    public void addMenu(String name, double price, String type, String category, String imageUrl) throws RepositoryException {
        menuRepository.addMenu(name, price, type, category, imageUrl);
    }

    public void deleteMenu(int id) throws RepositoryException {
        menuRepository.deleteMenu(id);
    }

    // ---- Order ----
    public List<Order> getAllOrders() throws RepositoryException {
        return orderRepository.findAll();
    }

    public void markOrderFinished(int orderId) throws RepositoryException {
        orderRepository.updateStatus(orderId, "finished");
    }
}
