package main.java.tubes.service;

import main.java.tubes.models.Order;

public interface PaymentProcessor {
    /**
     * Memproses transaksi pembayaran dan menyimpan data pesanan ke database PostgreSQL.
     * @param order Data pesanan yang akan dibayar
     * @param method Metode pembayaran (CASH, QRIS, CARD)
     * @return true jika berhasil disimpan, false jika gagal
     */
    boolean processTransaction(Order order, String method);
}