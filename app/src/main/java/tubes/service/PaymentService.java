package main.java.tubes.service;

public class PaymentService {
    public boolean executePayment(String method, double amount) {
        IPaymentProcessor processor;

        switch (method.toUpperCase()) {
            case "CASH":
                processor = new CashPaymentProcessor();
                break;
            case "QRIS":
                processor = new QRISPaymentProcessor();
                break;
            case "CARD":
            case "DEBIT/CREDIT":
                processor = new CardPaymentProcessor();
                break;
            default:
                return false;
        }

        return processor.process(amount);
    }
}