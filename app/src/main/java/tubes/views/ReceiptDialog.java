package main.java.tubes.views;

import main.java.tubes.models.CartItem;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReceiptDialog extends JDialog {

    public ReceiptDialog(Frame parent, List<CartItem> items, double totalAmount, String paymentMethod) {
        super(parent, "Struk Pembayaran - McDonald's", true);
        setSize(400, 560);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblHeader = new JLabel("McDonald's", JLabel.CENTER);
        lblHeader.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubHeader = new JLabel("==========================================", JLabel.CENTER);
        lblSubHeader.setFont(new Font("Monospaced", Font.PLAIN, 12));
        lblSubHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nomor order sederhana berbasis waktu (belum ada persistensi order ke DB)
        long orderNumber = System.currentTimeMillis() % 100000;
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        JLabel lblInfo = new JLabel("<html><center>No Order: #" + orderNumber + "<br>Tgl: " + timeStamp + "<br>Metode: " + paymentMethod + "</center></html>", JLabel.CENTER);
        lblInfo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(lblHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lblInfo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lblSubHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextArea txtItems = new JTextArea();
        txtItems.setEditable(false);
        txtItems.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();
        for (CartItem item : items) {
            String name = item.getMenu().getName();
            if (name.length() > 16) name = name.substring(0, 13) + "...";

            String strPrice = formatNominal(item.getTotalPrice());
            sb.append(String.format("%-16s %2dx  %12s\n", name, item.getQuantity(), strPrice));
        }

        sb.append("------------------------------------------\n");

        String strTotal = formatNominal(totalAmount);
        sb.append(String.format("%-18s    %12s\n", "TOTAL HARGA:", strTotal));
        sb.append("------------------------------------------\n\n");
        sb.append("         PEMBAYARAN BERHASIL / SUKSES\n");
        sb.append("            TERIMA KASIH ATAS\n");
        sb.append("             KUNJUNGAN ANDA!\n");

        txtItems.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(txtItems);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane);

        JButton btnClose = new JButton("Selesai & Tutup");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setBackground(new Color(255, 199, 44));
        btnClose.setFocusPainted(false);
        btnClose.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnClose.addActionListener(e -> dispose());

        add(mainPanel, BorderLayout.CENTER);
        add(btnClose, BorderLayout.SOUTH);
    }

    private String formatNominal(double harga) {
        return "Rp " + String.format("%,.0f", harga).replace(',', '.');
    }
}
