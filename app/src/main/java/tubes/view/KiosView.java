package main.java.tubes.view;

import main.java.tubes.controller.KiosController;
import main.java.tubes.model.Menu;
import main.java.tubes.model.CartItem;

// Import modul alurBayar milik teman
import alurBayar.models.Order;
import alurBayar.views.PaymentView;
import alurBayar.controllers.PaymentController;

javax.swing.*;
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

    // Palet Warna McD
    private final Color MCD_RED = new Color(218, 41, 28);
    private final Color MCD_YELLOW = new Color(255, 199, 44);
    private final Color BG_LIGHT = new Color(244, 244, 244);
    private final Color TEXT_DARK = new Color(30, 30, 30);

    public KiosView() {
        this.controller = new KiosController();
        
        setTitle("McDonald's Self-Service Kiosk System");
        setSize(1150, 800); 
        setMinimumSize(new Dimension(500, 500)); 
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

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateGridColumns();
            }
        });
    }

    // 1. HEADER
    private void initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 4, 0, MCD_YELLOW),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        JLabel logo = new JLabel("McDonald's Kiosk", JLabel.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(MCD_RED);
        header.add(logo, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
    }

    // 2. SIDEBAR KATEGORI
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

            try {
                URL imgURL = getClass().getResource("/assets/images/" + fileGambar);
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(img));
                } else {
                    File localFile = new File("src/main/resources/assets/images/" + fileGambar);
                    if (localFile.exists()) {
                        ImageIcon icon = new ImageIcon(localFile.getAbsolutePath());
                        Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                        btn.setIcon(new ImageIcon(img));
                    }
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Gagal memuat ikon: " + fileGambar);
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

    // 3. AREA GRID MENU
    private void initMenuGrid() {
        panelGridMenu = new JPanel(new GridLayout(0, 3, 20, 20)); 
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
        scrollMenu.getHorizontalScrollBar().setUnitIncrement(16);
        
        add(scrollMenu, BorderLayout.CENTER);
    }

    private void updateGridColumns() {
        if (scrollMenu == null || panelGridMenu == null) return;
        int viewWidth = scrollMenu.getViewport().getWidth() - 30; 
        if (viewWidth <= 0) return;
        int cols = Math.max(1, viewWidth / 240);
        panelGridMenu.setLayout(new GridLayout(0, cols, 20, 20));
        panelGridMenu.revalidate();
        panelGridMenu.repaint();
    }

    // 4. SIDEBAR KERANJANG & INTEGRASI CHECKOUT DATABASE
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

        labelTotalHarga = new JLabel("Total: $ 0.00", JLabel.CENTER);
        labelTotalHarga.setFont(new Font("Segoe UI", Font.BOLD, 20));
        labelTotalHarga.setForeground(TEXT_DARK);

        JButton btnCheckout = new JButton("Bayar / Checkout");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCheckout.setBackground(MCD_RED); 
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setFocusPainted(false);
        btnCheckout.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ========================================================
        // INTEGRASI PENYIMPANAN KE DATABASE SAAT CHECKOUT
        // ========================================================
        btnCheckout.addActionListener(e -> {
            if (controller.getCart().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keranjang kamu masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // 1. Ambil data keranjang dan hitung total
                double totalHarga = controller.calculateTotal();
                
                // 2. Buat objek Order yang membawa list item keranjang
                Order orderBaru = new Order();
                orderBaru.setTotalAmount(totalHarga);
                
                // 3. Panggil PaymentView untuk memilih metode pembayaran
                PaymentView paymentView = new PaymentView(orderBaru);
                paymentView.setVisible(true);

                // 4. Setelah lanjut pembayaran, simpan item keranjang ke database melalui Controller
                controller.simpanPesananKeDatabase(orderBaru);

                // 5. Bersihkan keranjang di UI setelah pesanan terkirim
                controller.clearCart();
                updateCartSidebar();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan pesanan ke Database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        summaryPanel.add(labelTotalHarga);
        summaryPanel.add(btnCheckout);
        rightPanel.add(summaryPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);
    }

    // 5. LOAD MENU DARI DATABASE VIA CONTROLLER
    private void loadMenuData() {
        panelGridMenu.removeAll();
        // Mengambil data menu yang ditarik dari Database
        List<Menu> listMenu = controller.getMenuBySubCategory(kategoriAktif);

        for (Menu m : listMenu) {
            JPanel card = new JPanel(new BorderLayout(0, 5));
            card.setBackground(Color.WHITE);
            
            Dimension cardSize = new Dimension(220, 260);
            card.setPreferredSize(cardSize);
            card.setMinimumSize(cardSize);
            card.setMaximumSize(cardSize);
            
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            // Gambar
            JPanel imagePanel = new JPanel(new GridBagLayout());
            imagePanel.setBackground(Color.WHITE);
            imagePanel.setPreferredSize(new Dimension(100, 100));

            JLabel labelGambar = new JLabel("", JLabel.CENTER);
            labelGambar.setPreferredSize(new Dimension(100, 100));

            String imgFileName = m.getImageUrl();
            if (imgFileName != null) {
                imgFileName = imgFileName.trim().replace(" ", "_");
                if (imgFileName.endsWith(".webp")) imgFileName = imgFileName.replace(".webp", ".png");
            }

            boolean imageLoaded = false;
            try {
                URL imgURL = getClass().getResource("/assets/images/" + imgFileName);
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image resizedImg = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    labelGambar.setIcon(new ImageIcon(resizedImg));
                    imageLoaded = true;
                }
            } catch (Exception e) {
                imageLoaded = false;
            }

            if (!imageLoaded) {
                labelGambar.setText("[ " + m.getName() + " ]");
                labelGambar.setFont(new Font("Segoe UI", Font.BOLD, 10));
                labelGambar.setForeground(Color.GRAY);
            }

            imagePanel.add(labelGambar);
            card.add(imagePanel, BorderLayout.NORTH);

            // Nama & Harga
            JPanel infoPanel = new JPanel(new GridLayout(2, 1, 2, 2));
            infoPanel.setBackground(Color.WHITE);
            infoPanel.setPreferredSize(new Dimension(200, 50)); 

            JLabel labelNama = new JLabel("<html><div style='text-align: center;'>" + m.getName() + "</div></html>", JLabel.CENTER);
            labelNama.setFont(new Font("Segoe UI", Font.BOLD, 12));

            JLabel labelHarga = new JLabel("$ " + String.format("%.2f", m.getPrice()), JLabel.CENTER);
            labelHarga.setFont(new Font("Segoe UI", Font.BOLD, 13));
            labelHarga.setForeground(MCD_RED);

            infoPanel.add(labelNama);
            infoPanel.add(labelHarga);
            card.add(infoPanel, BorderLayout.CENTER);

            // Kontrol Tambah (Add ke Keranjang)
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            controlPanel.setBackground(Color.WHITE);

            JLabel labelQty = new JLabel("1", JLabel.CENTER);
            labelQty.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelQty.setPreferredSize(new Dimension(20, 28));

            JButton btnMin = new JButton("-");
            btnMin.setPreferredSize(new Dimension(30, 28));
            btnMin.addActionListener(e -> {
                int q = Integer.parseInt(labelQty.getText());
                if (q > 1) labelQty.setText(String.valueOf(q - 1));
            });

            JButton btnPlus = new JButton("+");
            btnPlus.setPreferredSize(new Dimension(30, 28));
            btnPlus.addActionListener(e -> {
                int q = Integer.parseInt(labelQty.getText());
                labelQty.setText(String.valueOf(q + 1));
            });

            JButton btnAdd = new JButton("Add");
            btnAdd.setPreferredSize(new Dimension(55, 28));
            btnAdd.setBackground(MCD_YELLOW);
            btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnAdd.setFocusPainted(false);

            // AKSI ADD: Memasukkan item ke memori keranjang Kiosk
            btnAdd.addActionListener(e -> {
                int qty = Integer.parseInt(labelQty.getText());
                controller.addToCart(m, qty);
                updateCartSidebar();
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

    // 6. UPDATE KERANJANG
    private void updateCartSidebar() {
        panelIsiKeranjang.removeAll();
        List<CartItem> items = controller.getCart();

        if (items.isEmpty()) {
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

                JLabel labelSubTotal = new JLabel("$ " + String.format("%.2f", item.getTotalPrice()));
                labelSubTotal.setFont(new Font("Segoe UI", Font.BOLD, 11));
                labelSubTotal.setForeground(MCD_RED);

                infoPanel.add(labelNama);
                infoPanel.add(labelSubTotal);

                JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
                actionPanel.setBackground(Color.WHITE);

                JButton btnMinusCart = new JButton("-");
                btnMinusCart.setPreferredSize(new Dimension(24, 24));
                btnMinusCart.addActionListener(e -> {
                    if (item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                    } else {
                        controller.getCart().remove(item);
                    }
                    updateCartSidebar();
                });

                JLabel labelQtyCart = new JLabel(String.valueOf(item.getQuantity()), JLabel.CENTER);
                labelQtyCart.setPreferredSize(new Dimension(20, 24));

                JButton btnPlusCart = new JButton("+");
                btnPlusCart.setPreferredSize(new Dimension(24, 24));
                btnPlusCart.addActionListener(e -> {
                    item.setQuantity(item.getQuantity() + 1);
                    updateCartSidebar();
                });

                JButton btnDeleteCart = new JButton("✕");
                btnDeleteCart.setPreferredSize(new Dimension(24, 24));
                btnDeleteCart.setForeground(Color.WHITE);
                btnDeleteCart.setBackground(MCD_RED);
                btnDeleteCart.addActionListener(e -> {
                    controller.getCart().remove(item);
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

        labelTotalHarga.setText("Total: $ " + String.format("%.2f", controller.calculateTotal()));
        panelIsiKeranjang.revalidate();
        panelIsiKeranjang.repaint();
    }
}