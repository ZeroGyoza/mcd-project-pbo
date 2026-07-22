package main.java.tubes.service;

public class CardPaymentProcessor implements IPaymentProcessor {
    @Override
    public boolean process(double amount) {
        return amount > 0;
    }
}