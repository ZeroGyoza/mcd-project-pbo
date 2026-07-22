package main.java.tubes.views;

import main.java.tubes.controllers.AdminController;
import main.java.tubes.enums.MenuType;
import main.java.tubes.models.Category;
import main.java.tubes.models.Menu;
import main.java.tubes.models.Order;
import main.java.tubes.models.OrderItem;
import main.java.tubes.utils.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminView extends JFrame {
    private final AdminController controller = new AdminController();

    // --- tab Orders ---
    private DefaultTableModel orderTableModel;
    private JTable orderTable;

    // --- tab Kategori ---
    private DefaultTableModel categoryTableModel;
    private JTable categoryTable;
    private JTextField fieldCategoryName;

    // --- tab Menu ---
    private DefaultTableModel menuTableModel;
    private JTable menuTable;
    private JTextField fieldMenuName, fieldMenuPrice, fieldMenuImage;
    private JComboBox<MenuType> comboMenuType;
    private JComboBox<Category> comboMenuCategory;

    public AdminView() {
        setTitle("Admin Dashboard - McDonald's Kiosk (" + Session.getInstance().getCurrentAccount().getUsername() + ")");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Orders", buildOrderTab());
        tabs.addTab("Kategori", buildCategoryTab());
        tabs.addTab("Menu", buildMenuTab());
        add(tabs);

        refreshOrders();
        refreshCategories();
        refreshMenu();
    }

    // ==================== TAB ORDERS ====================
    private JPanel buildOrderTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        orderTableModel = new DefaultTableModel(new Object[]{"ID", "Status", "Total", "Waktu", "Item"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        orderTable = new JTable(orderTableModel);
        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnFinish = new JButton("Tandai Selesai (paid -> finished)");
        bottom.add(btnRefresh);
        bottom.add(btnFinish);
        panel.add(bottom, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> refreshOrders());
        btnFinish.addActionListener(e -> {
            int row = orderTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih order dulu");
                return;
            }
            int orderId = (int) orderTableModel.getValueAt(row, 0);
            String status = (String) orderTableModel.getValueAt(row, 1);
            if (!status.equalsIgnoreCase("paid")) {
                JOptionPane.showMessageDialog(this, "Order ini statusnya bukan 'paid'");
                return;
            }
            try {
                controller.markOrderFinished(orderId);
                refreshOrders();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal update status: " + ex.getMessage());
            }
        });

        return panel;
    }

    private void refreshOrders() {
        try {
            orderTableModel.setRowCount(0);
            NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            List<Order> orders = controller.getAllOrders();
            for (Order o : orders) {
                StringBuilder items = new StringBuilder();
                for (OrderItem it : o.getItems()) {
                    items.append(it.getMenuName()).append(" x").append(it.getQuantity()).append("; ");
                }
                orderTableModel.addRow(new Object[]{
                        o.getId(),
                        o.getStatus().toDbValue(),
                        rupiah.format(o.getTotalPrice()),
                        o.getCreatedAt(),
                        items.toString()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat order: " + ex.getMessage());
        }
    }

    // ==================== TAB KATEGORI ====================
    private JPanel buildCategoryTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        categoryTableModel = new DefaultTableModel(new Object[]{"ID", "Nama Kategori"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        categoryTable = new JTable(categoryTableModel);
        panel.add(new JScrollPane(categoryTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldCategoryName = new JTextField(15);
        JButton btnAdd = new JButton("Tambah");
        JButton btnDelete = new JButton("Hapus Terpilih");
        bottom.add(new JLabel("Nama:"));
        bottom.add(fieldCategoryName);
        bottom.add(btnAdd);
        bottom.add(btnDelete);
        panel.add(bottom, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            String name = fieldCategoryName.getText().trim();
            if (name.isEmpty()) return;
            try {
                controller.addCategory(name);
                fieldCategoryName.setText("");
                refreshCategories();
                refreshMenu(); // supaya combo box kategori di tab menu ikut update
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menambah kategori: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            int row = categoryTable.getSelectedRow();
            if (row < 0) return;
            int id = (int) categoryTableModel.getValueAt(row, 0);
            try {
                controller.deleteCategory(id);
                refreshCategories();
                refreshMenu();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus kategori: " + ex.getMessage());
            }
        });

        return panel;
    }

    private void refreshCategories() {
        try {
            categoryTableModel.setRowCount(0);
            List<Category> categories = controller.getAllCategories();
            for (Category c : categories) {
                categoryTableModel.addRow(new Object[]{c.getId(), c.getName()});
            }
            if (comboMenuCategory != null) {
                comboMenuCategory.removeAllItems();
                for (Category c : categories) comboMenuCategory.addItem(c);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat kategori: " + ex.getMessage());
        }
    }

    // ==================== TAB MENU ====================
    private JPanel buildMenuTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        menuTableModel = new DefaultTableModel(new Object[]{"ID", "Nama", "Tipe", "Kategori", "Harga"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        menuTable = new JTable(menuTableModel);
        panel.add(new JScrollPane(menuTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(2, 6, 6, 6));
        fieldMenuName = new JTextField();
        fieldMenuPrice = new JTextField();
        fieldMenuImage = new JTextField();
        comboMenuType = new JComboBox<>(MenuType.values());
        comboMenuCategory = new JComboBox<>();

        form.add(new JLabel("Nama"));
        form.add(new JLabel("Harga"));
        form.add(new JLabel("Tipe"));
        form.add(new JLabel("Kategori"));
        form.add(new JLabel("URL Gambar"));
        form.add(new JLabel(""));

        form.add(fieldMenuName);
        form.add(fieldMenuPrice);
        form.add(comboMenuType);
        form.add(comboMenuCategory);
        form.add(fieldMenuImage);

        JButton btnAdd = new JButton("Tambah Menu");
        form.add(btnAdd);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(form, BorderLayout.CENTER);
        JButton btnDelete = new JButton("Hapus Menu Terpilih");
        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomButtons.add(btnDelete);
        bottom.add(bottomButtons, BorderLayout.SOUTH);

        panel.add(bottom, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            try {
                String name = fieldMenuName.getText().trim();
                double price = Double.parseDouble(fieldMenuPrice.getText().trim());
                MenuType type = (MenuType) comboMenuType.getSelectedItem();
                Category category = (Category) comboMenuCategory.getSelectedItem();
                String image = fieldMenuImage.getText().trim();

                if (name.isEmpty() || category == null) {
                    JOptionPane.showMessageDialog(this, "Nama dan kategori wajib diisi");
                    return;
                }

                controller.addMenu(name, price, type.name(), category.getName(), image);
                fieldMenuName.setText("");
                fieldMenuPrice.setText("");
                fieldMenuImage.setText("");
                refreshMenu();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga harus berupa angka");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menambah menu: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            int row = menuTable.getSelectedRow();
            if (row < 0) return;
            int id = (int) menuTableModel.getValueAt(row, 0);
            try {
                controller.deleteMenu(id);
                refreshMenu();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus menu: " + ex.getMessage());
            }
        });

        return panel;
    }

    private void refreshMenu() {
        menuTableModel.setRowCount(0);
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        List<Menu> menus = controller.getAllMenu();
        for (Menu m : menus) {
            menuTableModel.addRow(new Object[]{
                    m.getId(), m.getName(), m.getType(), m.getSubCategory(), rupiah.format(m.getPrice())
            });
        }
    }
}
