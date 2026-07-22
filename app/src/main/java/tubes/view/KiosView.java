package main.java.tubes.view;

import main.java.tubes.controller.KiosController;
import main.java.tubes.model.Menu;
import main.java.tubes.model.CartItem;
import main.java.tubes.model.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.URL;
import java.util.List;

public class KiosView extends JFrame {
    private KiosController controller;
    private JPanel panelGridMenu;
    private JPanel panelIsiKeranjang;
    private JLabel labelTotalHarga;
    private JScrollPane scrollMenu;
    private String kategoriAktif = "All";

    // Palet Warna McDonald's
    private final Color MCD_RED = new Color(218, 41, 28);
    private final Color MCD_YELLOW = new Color(255, 199, 44);
    private final Color BG_LIGHT = new Color(244, 244, 244);
    private final Color TEXT_DARK = new Color(30, 30, 30);

    public KiosView() {
        this.controller = new KiosController();
        
        setTitle("McDonald's Self-Service Kiosk System");
        setSize(1150, 800); 
        setMinimumSize(new Dimension(600, 500)); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 0)); 
        getContentPane().setBackground(BG_LIGHT); 

        initHeader();
        initSidebarKategori(); 
        initMenuGrid();        
        initSidebarRightCart(); 

        loadMenuData();
        updateCartSidebar();

        // Responsive Resizing untuk Grid Menu
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateGridColumns();
            }
        });
    }

    private String formatHarga(double harga) {
        return "Rp " + String.format("%,.0f", harga).replace(',', '.');
    }

    private void initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 4, 0, MCD_YELLOW),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        JLabel logo = new JLabel("McDonald's", JLabel.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(MCD_RED);
        header.add(logo, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
    }

    private void initSidebarKategori() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(MCD_RED); 
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12));
        sidebar.setPreferredSize(new Dimension(180, 0));

        JScrollPane sidebarScroll = new JScrollPane(sidebar);
        sidebarScroll.setBorder(null);
        sidebarScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(12);

        JButton btnAdmin = new JButton("🔒 Login Admin");
        btnAdmin.setPreferredSize(new Dimension(156, 40));
        btnAdmin.setMaximumSize(new Dimension(156, 40));
        btnAdmin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdmin.setFocusPainted(false);
        btnAdmin.setBackground(new Color(160, 20, 15)); 
        btnAdmin.setForeground(Color.WHITE);
        btnAdmin.setBorder(BorderFactory.createLineBorder(MCD_YELLOW, 1));
        btnAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnAdmin.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Fitur Login Admin sedang aktif.", "Info Kiosk", JOptionPane.INFORMATION_MESSAGE);
        });
        sidebar.add(btnAdmin);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setMaximumSize(new Dimension(156, 1));
        separator.setForeground(MCD_YELLOW);
        sidebar.add(separator);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));

        String[][] kategoriData = {
            {"All", "all.png"},
            {"Burger", "burger.png"},
            {"Chicken", "chicken.png"},
            {"Sides", "sides.png"},
            {"Dessert", "dessert.png"},
            {"Beverage", "beverage.png"},
            {"McCafé", "mccafe.png"},
            {"Happy Meal", "happymeal.png"},
            {"Combo", "combo.png"}
        };

        for (String[] kat : kategoriData) {
            String namaKategori = kat[0];
            String fileGambar = kat[1];

            JButton btn = new JButton(namaKategori);
            btn.setPreferredSize(new Dimension(156, 48));
            btn.setMaximumSize(new Dimension(156, 48));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setForeground(TEXT_DARK);
            btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            btn.setHorizontalAlignment(SwingConstants.CENTER);
            btn.setHorizontalTextPosition(SwingConstants.RIGHT); 
            btn.setVerticalTextPosition(SwingConstants.CENTER);
            btn.setIconTextGap(8);

            ImageIcon katIcon = loadScaledImage(fileGambar, 24, 24);
            if (katIcon != null) {
                btn.setIcon(katIcon);
            }
            
            btn.addActionListener(e -> {
                kategoriAktif = namaKategori;
                loadMenuData();
            });
            
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        add(sidebarScroll, BorderLayout.WEST);
    }

    private void initMenuGrid() {
        panelGridMenu = new JPanel(new GridLayout(0, 4, 15, 15)); 
        panelGridMenu.setBackground(BG_LIGHT);

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(BG_LIGHT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST; 
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.insets = new Insets(15, 15, 15, 15);
        wrapperPanel.add(panelGridMenu, gbc);

        scrollMenu = new JScrollPane(wrapperPanel);
        scrollMenu.setBorder(null);
        scrollMenu.setBackground(BG_LIGHT);
        scrollMenu.getViewport().setBackground(BG_LIGHT);
        
        scrollMenu.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollMenu.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(16); 
        
        add(scrollMenu, BorderLayout.CENTER);
    }

    private void updateGridColumns() {
        if (scrollMenu == null || panelGridMenu == null) return;
        int viewWidth = scrollMenu.getViewport().getWidth() - 30; 
        if (viewWidth <= 0) return;
        int cols = Math.max(1, viewWidth / 220);
        panelGridMenu.setLayout(new GridLayout(0, cols, 15, 15));
        panelGridMenu.revalidate();
        panelGridMenu.repaint();
    }

    private void initSidebarRightCart() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(320, 0)); 
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(230, 230, 230)));

        JLabel titleCart = new JLabel("Keranjang Belanja", JLabel.CENTER);
        titleCart.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleCart.setForeground(TEXT_DARK);
        titleCart.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        rightPanel.add(titleCart, BorderLayout.NORTH);

        panelIsiKeranjang = new JPanel();
        panelIsiKeranjang.setLayout(new BoxLayout(panelIsiKeranjang, BoxLayout.Y_AXIS));
        panelIsiKeranjang.setBackground(Color.WHITE);
        panelIsiKeranjang.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane cartScroll = new JScrollPane(panelIsiKeranjang);
        cartScroll.setBorder(null);
        cartScroll.setBackground(Color.WHITE);
        cartScroll.getViewport().setBackground(Color.WHITE);
        rightPanel.add(cartScroll, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        summaryPanel.setBackground(MCD_YELLOW); 
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        labelTotalHarga = new JLabel("Total: Rp 0", JLabel.CENTER);
        labelTotalHarga.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTotalHarga.setForeground(TEXT_DARK);

        JButton btnCheckout = new JButton("Bayar / Checkout");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCheckout.setBackground(MCD_RED); 
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setFocusPainted(false);
        btnCheckout.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnCheckout.addActionListener(e -> {
            if (controller.getCart() == null || controller.getCart().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keranjang kamu masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Order orderBaru = new Order(controller.calculateTotal(), controller.getCart());
                
                PaymentView paymentView = new PaymentView(this, orderBaru);
                paymentView.setVisible(true);

                if (paymentView.isPaidSuccess()) {
                    controller.clearCart();
                    updateCartSidebar();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan transaksi: " + ex.getMessage(), "Payment Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        summaryPanel.add(labelTotalHarga);
        summaryPanel.add(btnCheckout);
        rightPanel.add(summaryPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);
    }

    private ImageIcon loadScaledImage(String fileName, int width, int height) {
        if (fileName == null || fileName.trim().isEmpty()) return null;
        
        String cleanName = fileName.trim();
        String lowerName = cleanName.toLowerCase();
        
        String[] possibleNames = {
            cleanName,
            lowerName,
            cleanName.replace(" ", "_"),
            lowerName.replace(" ", "_"),
            cleanName.replace(".webp", ".png"),
            lowerName.replace(".webp", ".png"),
            lowerName.replace(" ", "_").replace(".webp", ".png")
        };

        for (String name : possibleNames) {
            String[] filePaths = {
                "app/src/main/resources/assets/images/" + name,
                "app/src/main/resources/assets/" + name,
                "src/main/resources/assets/images/" + name,
                "src/main/resources/assets/" + name,
                "resources/assets/images/" + name
            };

            for (String path : filePaths) {
                File file = new File(path);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(img);
                }
            }

            String[] resourcePaths = {
                "/assets/images/" + name,
                "/assets/" + name,
                "/" + name
            };

            for (String path : resourcePaths) {
                try {
                    URL imgURL = getClass().getResource(path);
                    if (imgURL != null) {
                        ImageIcon icon = new ImageIcon(imgURL);
                        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        return new ImageIcon(img);
                    }
                } catch (Exception ignored) {}
            }
        }
        return null;
    }

    private void loadMenuData() {
        panelGridMenu.removeAll();
        List<Menu> listMenu = controller.getMenuBySubCategory(kategoriAktif);

        for (Menu m : listMenu) {
            JPanel card = new JPanel(new BorderLayout(0, 8));
            card.setBackground(Color.WHITE);
            
            Dimension cardSize = new Dimension(210, 270);
            card.setPreferredSize(cardSize);
            card.setMinimumSize(cardSize);
            card.setMaximumSize(cardSize);
            
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
            ));

            JPanel centerPanel = new JPanel(new BorderLayout(0, 5));
            centerPanel.setBackground(Color.WHITE);

            JLabel labelGambar = new JLabel("", JLabel.CENTER);
            ImageIcon menuIcon = loadScaledImage(m.getImageUrl(), 120, 110);

            if (menuIcon != null) {
                labelGambar.setIcon(menuIcon);
            } else {
                labelGambar.setText("<html><center>📷<br>" + m.getName() + "</center></html>");
                labelGambar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                labelGambar.setForeground(Color.GRAY);
            }

            centerPanel.add(labelGambar, BorderLayout.CENTER);

            JPanel infoPanel = new JPanel(new GridLayout(2, 1, 2, 4));
            infoPanel.setBackground(Color.WHITE);

            JLabel labelNama = new JLabel("<html><div style='text-align: center;'>" + m.getName() + "</div></html>", JLabel.CENTER);
            labelNama.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelNama.setForeground(TEXT_DARK);

            JLabel labelHarga = new JLabel(formatHarga(m.getPrice()), JLabel.CENTER);
            labelHarga.setFont(new Font("Segoe UI", Font.BOLD, 13));
            labelHarga.setForeground(MCD_RED);

            infoPanel.add(labelNama);
            infoPanel.add(labelHarga);
            centerPanel.add(infoPanel, BorderLayout.SOUTH);

            card.add(centerPanel, BorderLayout.CENTER);

            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
            controlPanel.setBackground(Color.WHITE);

            final int[] count = {1};

            JLabel labelQty = new JLabel("1", JLabel.CENTER);
            labelQty.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelQty.setPreferredSize(new Dimension(16, 26));

            JButton btnMin = new JButton("-");
            btnMin.setMargin(new Insets(2, 6, 2, 6));
            btnMin.setFont(new Font("Arial", Font.BOLD, 14));
            btnMin.setFocusPainted(false);
            btnMin.setBackground(new Color(240, 240, 240));
            btnMin.addActionListener(e -> {
                if (count[0] > 1) {
                    count[0]--;
                    labelQty.setText(String.valueOf(count[0]));
                }
            });

            JButton btnPlus = new JButton("+");
            btnPlus.setMargin(new Insets(2, 6, 2, 6));
            btnPlus.setFont(new Font("Arial", Font.BOLD, 14));
            btnPlus.setFocusPainted(false);
            btnPlus.setBackground(new Color(240, 240, 240));
            btnPlus.addActionListener(e -> {
                count[0]++;
                labelQty.setText(String.valueOf(count[0]));
            });

            JButton btnAdd = new JButton("Add");
            btnAdd.setMargin(new Insets(2, 8, 2, 8));
            btnAdd.setBackground(MCD_YELLOW);
            btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnAdd.setFocusPainted(false);
            btnAdd.setBorder(BorderFactory.createLineBorder(new Color(230, 180, 0), 1));

            // Menambahkan item ke Database PostgreSQL & RAM
            btnAdd.addActionListener(e -> {
                controller.addToCart(m, count[0]);
                updateCartSidebar();
                count[0] = 1;
                labelQty.setText("1");
            });

            controlPanel.add(btnMin);
            controlPanel.add(labelQty);
            controlPanel.add(btnPlus);
            controlPanel.add(btnAdd);

            card.add(controlPanel, BorderLayout.SOUTH);
            panelGridMenu.add(card);
        }

        updateGridColumns();
    }

    private void updateCartSidebar() {
        panelIsiKeranjang.removeAll();
        List<CartItem> items = controller.getCart();

        if (items == null || items.isEmpty()) {
            JLabel emptyLabel = new JLabel("Keranjang Kosong", JLabel.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            emptyLabel.setForeground(Color.GRAY);
            panelIsiKeranjang.add(emptyLabel);
        } else {
            for (CartItem item : items) {
                JPanel itemRow = new JPanel(new BorderLayout(5, 0));
                itemRow.setBackground(Color.WHITE);
                itemRow.setMaximumSize(new Dimension(300, 50));
                itemRow.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

                JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 0));
                infoPanel.setBackground(Color.WHITE);

                JLabel labelNama = new JLabel(item.getMenu().getName());
                labelNama.setFont(new Font("Segoe UI", Font.BOLD, 12));

                JLabel labelSubTotal = new JLabel(formatHarga(item.getTotalPrice()));
                labelSubTotal.setFont(new Font("Segoe UI", Font.BOLD, 11));
                labelSubTotal.setForeground(MCD_RED);

                infoPanel.add(labelNama);
                infoPanel.add(labelSubTotal);

                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
                actionPanel.setBackground(Color.WHITE);

                // Tombol (-)
                JButton btnMinusCart = new JButton("-");
                btnMinusCart.setMargin(new Insets(1, 5, 1, 5));
                btnMinusCart.setFont(new Font("Arial", Font.BOLD, 12));
                btnMinusCart.setFocusPainted(false);
                btnMinusCart.addActionListener(e -> {
                    controller.updateItemQuantity(item, item.getQuantity() - 1);
                    updateCartSidebar();
                });

                JLabel labelQtyCart = new JLabel(String.valueOf(item.getQuantity()), JLabel.CENTER);
                labelQtyCart.setFont(new Font("Segoe UI", Font.BOLD, 12));
                labelQtyCart.setPreferredSize(new Dimension(16, 24));

                // Tombol (+)
                JButton btnPlusCart = new JButton("+");
                btnPlusCart.setMargin(new Insets(1, 5, 1, 5));
                btnPlusCart.setFont(new Font("Arial", Font.BOLD, 12));
                btnPlusCart.setFocusPainted(false);
                btnPlusCart.addActionListener(e -> {
                    controller.updateItemQuantity(item, item.getQuantity() + 1);
                    updateCartSidebar();
                });

                // Tombol (X)
                JButton btnDeleteCart = new JButton("X");
                btnDeleteCart.setMargin(new Insets(1, 6, 1, 6));
                btnDeleteCart.setFont(new Font("Arial", Font.BOLD, 11));
                btnDeleteCart.setFocusPainted(false);
                btnDeleteCart.setForeground(Color.WHITE);
                btnDeleteCart.setBackground(MCD_RED);
                btnDeleteCart.addActionListener(e -> {
                    controller.removeItemFromCart(item);
                    updateCartSidebar();
                });

                actionPanel.add(btnMinusCart);
                actionPanel.add(labelQtyCart);
                actionPanel.add(btnPlusCart);
                actionPanel.add(btnDeleteCart);

                itemRow.add(infoPanel, BorderLayout.CENTER);
                itemRow.add(actionPanel, BorderLayout.EAST);

                panelIsiKeranjang.add(itemRow);
                panelIsiKeranjang.add(Box.createRigidArea(new Dimension(0, 4)));
            }
        }

        labelTotalHarga.setText("Total: " + formatHarga(controller.calculateTotal()));
        panelIsiKeranjang.revalidate();
        panelIsiKeranjang.repaint();
    }
}