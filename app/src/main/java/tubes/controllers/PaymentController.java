package main.java.tubes.controllers;

import main.java.tubes.service.PaymentService;

public class PaymentController {
    private PaymentService paymentService;

    public PaymentController() {
        this.paymentService = new PaymentService();
    }

    /**
     * Memproses pembayaran lewat strategy PaymentService (Cash/QRIS/Card).
     * @param totalAmount total tagihan keranjang
     * @param method metode pembayaran: "CASH", "QRIS", atau "CARD"
     * @return true jika pembayaran berhasil
     */
    public boolean processTransaction(double totalAmount, String method) {
        return paymentService.executePayment(method, totalAmount);
    }
}
