package main.java.tubes.service;

public interface IPaymentProcessor {
    boolean process(double amount);
}