package main.java.tubes.view;

import main.java.tubes.controller.PaymentController;
import main.java.tubes.model.Order;

import javax.swing.*;
import java.awt.*;

public class PaymentView extends JDialog {
    private Order order;
    private PaymentController paymentController; // Deklarasi variabel controller
    private boolean isPaidSuccess = false;

    // Palet Warna McD
    private final Color MCD_RED = new Color(218, 41, 28);
    private final Color MCD_YELLOW = new Color(255, 199, 44);

    public PaymentView(Frame parent, Order order) {
        super(parent, "Metode Pembayaran - McDonald's", true);
        this.order = order;
        
        // Inisialisasi controller langsung tanpa interface
        this.paymentController = new PaymentController(); 

        setSize(420, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initUI();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel();
        header.setBackground(MCD_RED);
        header.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        
        JLabel title = new JLabel("PILIH METODE PEMBAYARAN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Content
        JPanel body = new JPanel(new GridLayout(4, 1, 10, 10));
        body.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        body.setBackground(Color.WHITE);

        JLabel totalLabel = new JLabel("Total Tagihan: $ " + String.format("%.2f", order.getTotalAmount()), JLabel.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(new Color(30, 30, 30));
        body.add(totalLabel);

        JButton btnCash = new JButton("💵 Cash / Tunai");
        JButton btnQris = new JButton("📱 QRIS / E-Wallet");
        JButton btnCard = new JButton("💳 Kartu Debit / Kredit");

        styleButton(btnCash);
        styleButton(btnQris);
        styleButton(btnCard);

        // Action Listener tombol pembayaran
        btnCash.addActionListener(e -> processPayment("CASH"));
        btnQris.addActionListener(e -> processPayment("QRIS"));
        btnCard.addActionListener(e -> processPayment("CARD"));

        body.add(btnCash);
        body.add(btnQris);
        body.add(btnCard);

        add(body, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(MCD_YELLOW);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(230, 180, 0), 1));
    }

    private void processPayment(String method) {
        // Panggil method dari controller langsung
        boolean success = paymentController.processTransaction(order, method);
        
        if (success) {
            isPaidSuccess = true;
            Frame owner = (Frame) getOwner();
            
            // 1. Tutup pop-up pilihan pembayaran
            dispose(); 
            
            // 2. Langsung tampilkan dialog Struk Belanja
            ReceiptDialog receipt = new ReceiptDialog(owner, order);
            receipt.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                this, 
                "Pembayaran / Penyimpanan ke Database Gagal!\nPastikan koneksi PostgreSQL aktif.", 
                "Error Transaksi", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isPaidSuccess() {
        return isPaidSuccess;
    }
}