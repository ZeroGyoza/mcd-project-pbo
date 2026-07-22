package main.java.tubes.controller;

import main.java.tubes.model.Order;
import main.java.tubes.repositories.OrderRepository;
import main.java.tubes.service.PaymentProcessor;

public class PaymentController implements PaymentProcessor {
    private OrderRepository orderRepository;

    public PaymentController() {
        this.orderRepository = new OrderRepository();
    }

    @Override
    public boolean processTransaction(Order order, String method) {
        if (order == null || order.getItems().isEmpty()) {
            return false;
        }
        order.setPaymentMethod(method);
        order.setStatus("SUCCESS");
        
        return orderRepository.saveOrder(order);
    }
}