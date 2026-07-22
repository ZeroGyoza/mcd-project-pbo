package main.java.tubes.views;
import main.java.tubes.controllers.KiosController;
import main.java.tubes.models.CartItem;
import main.java.tubes.models.Menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    // ========================================================
    // PALET WARNA RESMI MCDONALD'S
    // ========================================================
    private final Color MCD_RED = new Color(218, 41, 28);       // Merah Utama McD
    private final Color MCD_YELLOW = new Color(255, 199, 44);   // Kuning Emas McD
    private final Color BG_LIGHT = new Color(244, 244, 244);     // Abu-abu terang
    private final Color TEXT_DARK = new Color(30, 30, 30);       // Teks Gelap

    public KiosView() {
        this.controller = new KiosController();
        
        setTitle("McDonald's Self-Service Kiosk");
        setSize(1150, 800); 
        setMinimumSize(new Dimension(900, 600)); 
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

        // Mengatur ulang jumlah kolom grid saat layar di-resize / full screen
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

    // 2. SIDEBAR KIRI: Kategori Menu
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

        // Tombol Login Admin
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
            JOptionPane.showMessageDialog(this, 
                "Fitur Login Admin sedang diintegrasikan oleh tim pengembang.", 
                "Info Kiosk", JOptionPane.INFORMATION_MESSAGE);
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
                    File localFile = new File("app/src/main/resources/assets/images/" + fileGambar);
                    if (!localFile.exists()) {
                        localFile = new File("src/main/resources/assets/images/" + fileGambar);
                    }
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

    // 3. AREA TENGAH: Grid Menu (Teratur, Tidak Melar & Tidak Terus ke Samping)
    private void initMenuGrid() {
        panelGridMenu = new JPanel(new GridLayout(0, 3, 20, 20)); // Default 3 Kolom
        panelGridMenu.setBackground(BG_LIGHT);

        // Wrapper Panel untuk menahan agar grid berada di pojok kiri atas dan tidak ditarik vertikal
        JPanel topAlignWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topAlignWrapper.setBackground(BG_LIGHT);
        topAlignWrapper.add(panelGridMenu);

        scrollMenu = new JScrollPane(topAlignWrapper);
        scrollMenu.setBorder(new EmptyBorder(15, 15, 15, 15));
        scrollMenu.setBackground(BG_LIGHT);
        scrollMenu.getViewport().setBackground(BG_LIGHT);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(16); 
        
        add(scrollMenu, BorderLayout.CENTER);
    }

    // Mengalkulasi jumlah kolom berdasarkan lebar viewport agar rapi dan tidak melar
    private void updateGridColumns() {
        if (scrollMenu == null || panelGridMenu == null) return;

        int viewWidth = scrollMenu.getViewport().getWidth() - 30; // Kurangi padding
        if (viewWidth <= 0) return;

        // Setiap kartu ukurannya fixed 220px + gap 20px = 240px
        int targetCardWidth = 240; 
        int cols = Math.max(1, viewWidth / targetCardWidth);

        // Atur layout grid sesuai kolom yang muat
        panelGridMenu.setLayout(new GridLayout(0, cols, 20, 20));
        panelGridMenu.revalidate();
        panelGridMenu.repaint();
    }

    // 4. SIDEBAR KANAN: Keranjang Belanja
    private void initSidebarRightCart() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 0));
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
        
        btnCheckout.addActionListener(e -> {
            if (controller.getCart().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keranjang kamu masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int respon = JOptionPane.showConfirmDialog(this, 
                "Total Pembayaran: $ " + String.format("%.2f", controller.calculateTotal()) + "\nLanjutkan ke mesin pembayaran?", 
                "Konfirmasi Pembayaran", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
            if (respon == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Pembayaran Berhasil! Silakan ambil struk Anda.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                controller.clearCart();
                updateCartSidebar();
            }
        });

        summaryPanel.add(labelTotalHarga);
        summaryPanel.add(btnCheckout);
        rightPanel.add(summaryPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);
    }

    // 5. LOAD MENU DATA
    private void loadMenuData() {
        panelGridMenu.removeAll();
        List<Menu> listMenu = controller.getMenuBySubCategory(kategoriAktif);

        for (Menu m : listMenu) {
            // Container Kartu Ukuran Terkunci Rapi (220 x 260 px)
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

            // A. CONTAINER GAMBAR (100x100)
            JPanel imagePanel = new JPanel(new GridBagLayout());
            imagePanel.setBackground(Color.WHITE);
            imagePanel.setPreferredSize(new Dimension(100, 100));

            JLabel labelGambar = new JLabel("", JLabel.CENTER);
            labelGambar.setPreferredSize(new Dimension(100, 100));

            String imgFileName = m.getImageUrl();
            if (imgFileName != null) {
                imgFileName = imgFileName.trim().replace(" ", "_");
                if (imgFileName.endsWith(".webp")) {
                    imgFileName = imgFileName.replace(".webp", ".png");
                }
            }

            boolean imageLoaded = false;
            try {
                URL imgURL = getClass().getResource("/assets/images/" + imgFileName);
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image resizedImg = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    labelGambar.setIcon(new ImageIcon(resizedImg));
                    imageLoaded = true;
                } else {
                    File fileDirect = new File("app/src/main/resources/assets/images/" + imgFileName);
                    if (!fileDirect.exists()) {
                        fileDirect = new File("src/main/resources/assets/images/" + imgFileName);
                    }
                    if (fileDirect.exists()) {
                        ImageIcon icon = new ImageIcon(fileDirect.getAbsolutePath());
                        Image resizedImg = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        labelGambar.setIcon(new ImageIcon(resizedImg));
                        imageLoaded = true;
                    }
                }
            } catch (Exception e) {
                imageLoaded = false;
            }

            if (!imageLoaded) {
                labelGambar.setText("[ No Image ]");
                labelGambar.setFont(new Font("Segoe UI", Font.BOLD, 10));
                labelGambar.setForeground(Color.LIGHT_GRAY);
            }

            imagePanel.add(labelGambar);
            card.add(imagePanel, BorderLayout.NORTH);

            // B. CONTAINER TEKS NAMA & HARGA (Tinggi 50px)
            JPanel infoPanel = new JPanel(new GridLayout(2, 1, 2, 2));
            infoPanel.setBackground(Color.WHITE);
            infoPanel.setPreferredSize(new Dimension(200, 50)); 

            JLabel labelNama = new JLabel("<html><div style='text-align: center;'>" + m.getName() + "</div></html>", JLabel.CENTER);
            labelNama.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelNama.setForeground(TEXT_DARK);

            JLabel labelHarga = new JLabel("$ " + String.format("%.2f", m.getPrice()), JLabel.CENTER);
            labelHarga.setFont(new Font("Segoe UI", Font.BOLD, 13));
            labelHarga.setForeground(MCD_RED);

            infoPanel.add(labelNama);
            infoPanel.add(labelHarga);
            card.add(infoPanel, BorderLayout.CENTER);

            // C. CONTAINER TOMBOL (- 1 + Add)
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            controlPanel.setBackground(Color.WHITE);

            JLabel labelQty = new JLabel("1", JLabel.CENTER);
            labelQty.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelQty.setPreferredSize(new Dimension(20, 28));

            JButton btnMin = new JButton("-");
            btnMin.setPreferredSize(new Dimension(30, 28));
            btnMin.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnMin.setBackground(BG_LIGHT);
            btnMin.setFocusPainted(false);
            btnMin.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
            btnMin.addActionListener(e -> {
                int q = Integer.parseInt(labelQty.getText());
                if (q > 1) labelQty.setText(String.valueOf(q - 1));
            });

            JButton btnPlus = new JButton("+");
            btnPlus.setPreferredSize(new Dimension(30, 28));
            btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnPlus.setBackground(BG_LIGHT);
            btnPlus.setFocusPainted(false);
            btnPlus.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
            btnPlus.addActionListener(e -> {
                int q = Integer.parseInt(labelQty.getText());
                labelQty.setText(String.valueOf(q + 1));
            });

            JButton btnAdd = new JButton("Add");
            btnAdd.setPreferredSize(new Dimension(55, 28));
            btnAdd.setBackground(MCD_YELLOW);
            btnAdd.setForeground(TEXT_DARK);
            btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnAdd.setFocusPainted(false);
            btnAdd.setBorder(BorderFactory.createLineBorder(new Color(240, 180, 20)));

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

    // 6. UPDATE KERANJANG REAL-TIME
    private void updateCartSidebar() {
        panelIsiKeranjang.removeAll();
        List<CartItem> items = controller.getCart();

        for (CartItem item : items) {
            JPanel itemRow = new JPanel(new BorderLayout(10, 5));
            itemRow.setBackground(Color.WHITE);
            itemRow.setMaximumSize(new Dimension(280, 45));
            itemRow.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
                BorderFactory.createEmptyBorder(6, 5, 6, 5)
            ));

            String textNamaQty = item.getQuantity() + "x  " + item.getMenu().getName();
            JLabel labelDetail = new JLabel(textNamaQty);
            labelDetail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            labelDetail.setForeground(TEXT_DARK);

            JLabel labelSubTotal = new JLabel("$ " + String.format("%.2f", item.getTotalPrice()));
            labelSubTotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
            labelSubTotal.setForeground(MCD_RED);

            itemRow.add(labelDetail, BorderLayout.CENTER);
            itemRow.add(labelSubTotal, BorderLayout.EAST);

            panelIsiKeranjang.add(itemRow);
        }

        labelTotalHarga.setText("Total: $ " + String.format("%.2f", controller.calculateTotal()));

        panelIsiKeranjang.revalidate();
        panelIsiKeranjang.repaint();
    }
}