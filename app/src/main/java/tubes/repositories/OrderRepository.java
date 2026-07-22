package main.java.tubes.repositories;

import main.java.tubes.exceptions.RepositoryException;
import main.java.tubes.models.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> findAll() throws RepositoryException;
    void updateStatus(int orderId, String newStatus) throws RepositoryException;
}
