package main.java.tubes.views;

import main.java.tubes.controllers.PaymentController;
import main.java.tubes.models.CartItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PaymentView extends JDialog {
    private List<CartItem> cartItems;
    private double totalAmount;
    private PaymentController paymentController;
    private boolean isPaidSuccess = false;
    private String chosenMethod = null;

    // Palet Warna McD
    private final Color MCD_RED = new Color(218, 41, 28);
    private final Color MCD_YELLOW = new Color(255, 199, 44);

    public PaymentView(Frame parent, List<CartItem> cartItems, double totalAmount) {
        super(parent, "Metode Pembayaran - McDonald's", true);
        this.cartItems = cartItems;
        this.totalAmount = totalAmount;
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

        JLabel totalLabel = new JLabel("Total Tagihan: $ " + String.format("%.2f", totalAmount), JLabel.CENTER);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(new Color(30, 30, 30));
        body.add(totalLabel);

        JButton btnCash = new JButton("💵 Cash / Tunai");
        JButton btnQris = new JButton("📱 QRIS / E-Wallet");
        JButton btnCard = new JButton("💳 Kartu Debit / Kredit");

        styleButton(btnCash);
        styleButton(btnQris);
        styleButton(btnCard);

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
        boolean success = paymentController.processTransaction(totalAmount, method);

        if (success) {
            isPaidSuccess = true;
            chosenMethod = method;
            Frame owner = (Frame) getOwner();

            // 1. Tutup pop-up pilihan pembayaran
            dispose();

            // 2. Langsung tampilkan dialog Struk Belanja
            ReceiptDialog receipt = new ReceiptDialog(owner, cartItems, totalAmount, method);
            receipt.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Pembayaran Gagal! Silakan coba lagi.",
                "Error Transaksi",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isPaidSuccess() {
        return isPaidSuccess;
    }

    public String getChosenMethod() {
        return chosenMethod;
    }
}
