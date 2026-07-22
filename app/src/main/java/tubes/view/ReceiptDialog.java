package main.java.tubes.view;

import main.java.tubes.model.CartItem;
import main.java.tubes.model.Order;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptDialog extends JDialog {

    public ReceiptDialog(Frame parent, Order order) {
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

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        JLabel lblInfo = new JLabel("<html><center>No Order: #" + order.getId() + "<br>Tgl: " + timeStamp + "<br>Metode: " + order.getPaymentMethod() + "</center></html>", JLabel.CENTER);
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
        for (CartItem item : order.getItems()) {
            String name = item.getMenu().getName();
            if (name.length() > 16) name = name.substring(0, 13) + "...";
            
            String strPrice = formatNominal(item.getTotalPrice());
            sb.append(String.format("%-16s %2dx  %12s\n", name, item.getQuantity(), strPrice));
        }

        sb.append("------------------------------------------\n");
        
        String strTotal = formatNominal(order.getTotalAmount());
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