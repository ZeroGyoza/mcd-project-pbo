package main.java.tubes.view;

import main.java.tubes.controller.OrderController;
import main.java.tubes.model.CartItem;
import main.java.tubes.model.Menu;
import main.java.tubes.model.Order;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.List;

public class MenuView extends JFrame {
    private OrderController orderController;
    private Order currentOrder;
    
    private JPanel menuGridPanel;
    private JPanel cartItemsPanel;
    private JLabel totalAmountLabel;

    private final Color MCD_RED = new Color(218, 41, 28);
    private final Color MCD_YELLOW = new Color(255, 199, 44);

    public MenuView() {
        this.orderController = new OrderController();
        this.currentOrder = new Order();

        setTitle("McDonald's");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initUI();
        loadMenuItems("ALL");
    }

    private void initUI() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MCD_RED);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("McDonald's");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        JPanel mainContentPanel = new JPanel(new BorderLayout());

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        categoryPanel.setBackground(new Color(245, 245, 245));

        String[] categories = {"ALL", "Makanan", "Minuman", "Dessert"};
        for (String cat : categories) {
            JButton btnCat = new JButton(cat);
            btnCat.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnCat.setBackground(Color.WHITE);
            btnCat.setFocusPainted(false);
            btnCat.addActionListener(e -> loadMenuItems(cat));
            categoryPanel.add(btnCat);
        }

        mainContentPanel.add(categoryPanel, BorderLayout.NORTH);

        menuGridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        menuGridPanel.setBackground(Color.WHITE);
        menuGridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane menuScrollPane = new JScrollPane(menuGridPanel);
        menuScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainContentPanel.add(menuScrollPane, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setPreferredSize(new Dimension(320, 0));
        cartPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
        cartPanel.setBackground(Color.WHITE);

        JLabel cartTitle = new JLabel("Keranjang Pesanan", JLabel.CENTER);
        cartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cartTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        cartPanel.add(cartTitle, BorderLayout.NORTH);

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(Color.WHITE);

        JScrollPane cartScrollPane = new JScrollPane(cartItemsPanel);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        JPanel cartFooter = new JPanel(new BorderLayout(10, 10));
        cartFooter.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        cartFooter.setBackground(Color.WHITE);

        totalAmountLabel = new JLabel("Total: $ 0.00");
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnCheckout = new JButton("Bayar Sekarang");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCheckout.setBackground(MCD_YELLOW);
        btnCheckout.setFocusPainted(false);
        btnCheckout.addActionListener(e -> openPaymentView());

        cartFooter.add(totalAmountLabel, BorderLayout.NORTH);
        cartFooter.add(btnCheckout, BorderLayout.SOUTH);

        cartPanel.add(cartFooter, BorderLayout.SOUTH);

        add(cartPanel, BorderLayout.EAST);
    }

    private void loadMenuItems(String category) {
        menuGridPanel.removeAll();
        List<Menu> menus = orderController.getMenusByCategory(category);

        for (Menu menu : menus) {
            JPanel card = new JPanel(new BorderLayout(5, 5));
            card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
            card.setBackground(Color.WHITE);

            JLabel imgLabel = new JLabel();
            imgLabel.setHorizontalAlignment(JLabel.CENTER);
            imgLabel.setPreferredSize(new Dimension(140, 120));
            
            ImageIcon icon = loadImageAsset(menu.getImagePath());
            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(scaled));
            } else {
                imgLabel.setText("No Image");
                imgLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            }

            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel(menu.getName(), JLabel.CENTER);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

            JLabel priceLabel = new JLabel(String.format("$ %.2f", menu.getPrice()), JLabel.CENTER);
            priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            priceLabel.setForeground(MCD_RED);

            infoPanel.add(nameLabel);
            infoPanel.add(priceLabel);

            JButton btnAdd = new JButton("Tambah");
            btnAdd.setBackground(MCD_YELLOW);
            btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnAdd.setFocusPainted(false);
            btnAdd.addActionListener(e -> {
                currentOrder.addItem(menu, 1);
                updateCartUI();
            });

            card.add(imgLabel, BorderLayout.NORTH);
            card.add(infoPanel, BorderLayout.CENTER);
            card.add(btnAdd, BorderLayout.SOUTH);

            menuGridPanel.add(card);
        }

        menuGridPanel.revalidate();
        menuGridPanel.repaint();
    }

    private ImageIcon loadImageAsset(String fileName) {
        if (fileName == null || fileName.isEmpty()) return null;

        String relativePath = "src/main/resources/assets/images/" + fileName;
        File file = new File(relativePath);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }

        String resourcePath = "/assets/images/" + fileName;
        URL resourceUrl = getClass().getResource(resourcePath);
        if (resourceUrl != null) {
            return new ImageIcon(resourceUrl);
        }

        return null;
    }

    private void updateCartUI() {
        cartItemsPanel.removeAll();

        for (CartItem item : currentOrder.getItems()) {
            JPanel itemRow = new JPanel(new BorderLayout(5, 5));
            itemRow.setMaximumSize(new Dimension(300, 35));
            itemRow.setBackground(Color.WHITE);
            itemRow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));

            JLabel lblName = new JLabel(item.getMenu().getName() + " x" + item.getQuantity());
            lblName.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            JLabel lblPrice = new JLabel(String.format("$ %.2f", item.getTotalPrice()));
            lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 12));

            itemRow.add(lblName, BorderLayout.WEST);
            itemRow.add(lblPrice, BorderLayout.EAST);

            cartItemsPanel.add(itemRow);
        }

        totalAmountLabel.setText(String.format("Total: $ %.2f", currentOrder.getTotalAmount()));

        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private void openPaymentView() {
        if (currentOrder.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PaymentView paymentView = new PaymentView(this, currentOrder);
        paymentView.setVisible(true);

        if (paymentView.isPaidSuccess()) {
            this.currentOrder = new Order();
            updateCartUI();
        }
    }
}