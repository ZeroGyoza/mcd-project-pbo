package main.java.tubes.service;

public class CashPaymentProcessor implements IPaymentProcessor {
    @Override
    public boolean process(double amount) {
        return amount > 0;
    }
}