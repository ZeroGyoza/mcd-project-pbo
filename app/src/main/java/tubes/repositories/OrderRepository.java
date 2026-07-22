package main.java.tubes.repositories;

import main.java.tubes.model.CartItem;
import main.java.tubes.model.Order;
import main.java.tubes.utils.Database;

import java.sql.*;

public class OrderRepository {

    public boolean saveOrder(Order order) {
        String insertOrderSql = "INSERT INTO orders (total_amount, payment_method, status) VALUES (?, ?, ?)";
        String insertOrderItemSql = "INSERT INTO order_items (order_id, menu_id, quantity, price) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = Database.connect();
            if (conn == null) {
                System.err.println("[ERROR DB] Koneksi database null!");
                return false;
            }
            conn.setAutoCommit(false); // Transaksi manual

            // 1. Simpan Header Order
            try (PreparedStatement pstmtOrder = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmtOrder.setDouble(1, order.getTotalAmount());
                pstmtOrder.setString(2, order.getPaymentMethod() != null ? order.getPaymentMethod() : "CASH");
                pstmtOrder.setString(3, order.getStatus() != null ? order.getStatus() : "SUCCESS");
                
                int affectedRows = pstmtOrder.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                int orderId = -1;
                try (ResultSet generatedKeys = pstmtOrder.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                        order.setId(orderId);
                    }
                }

                if (orderId == -1) {
                    conn.rollback();
                    return false;
                }

                // 2. Simpan Detail Item Order
                try (PreparedStatement pstmtItem = conn.prepareStatement(insertOrderItemSql)) {
                    for (CartItem item : order.getItems()) {
                        pstmtItem.setInt(1, orderId);
                        pstmtItem.setInt(2, item.getMenu().getId());
                        pstmtItem.setInt(3, item.getQuantity());
                        pstmtItem.setDouble(4, item.getMenu().getPrice());
                        pstmtItem.addBatch();
                    }
                    pstmtItem.executeBatch();
                }

                conn.commit(); // Berhasil simpan semua
                System.out.println("[SUCCESS DB] Pesanan ID " + orderId + " berhasil tersimpan!");
                return true;

            } catch (SQLException ex) {
                if (conn != null) conn.rollback();
                System.err.println("[ERROR SQL Transaction] " + ex.getMessage());
                ex.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR DB Connection] " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}