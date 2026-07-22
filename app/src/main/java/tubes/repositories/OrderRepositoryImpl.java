package main.java.tubes.repositories;

import main.java.tubes.enums.OrderStatus;
import main.java.tubes.exceptions.RepositoryException;
import main.java.tubes.models.Order;
import main.java.tubes.models.OrderItem;
import main.java.tubes.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public List<Order> findAll() throws RepositoryException {
        String sqlOrders = "SELECT * FROM orders ORDER BY id DESC";
        String sqlItems = "SELECT * FROM order_items WHERE order_id = ? ORDER BY id ASC";

        List<Order> orders = new ArrayList<>();
        try (Connection conn = Database.connect()) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlOrders)) {
                while (rs.next()) {
                    orders.add(new Order(
                            rs.getInt("id"),
                            OrderStatus.fromDbValue(rs.getString("status")),
                            rs.getDouble("total_price"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
            }

            // ambil item per order (dipisah biar simpel, ga perlu JOIN + grouping manual)
            try (PreparedStatement ps = conn.prepareStatement(sqlItems)) {
                for (Order order : orders) {
                    ps.setInt(1, order.getId());
                    try (ResultSet rs = ps.executeQuery()) {
                        List<OrderItem> items = new ArrayList<>();
                        while (rs.next()) {
                            items.add(new OrderItem(
                                    rs.getInt("id"),
                                    rs.getInt("order_id"),
                                    rs.getString("menu_name"),
                                    rs.getDouble("price"),
                                    rs.getInt("quantity")
                            ));
                        }
                        order.setItems(items);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Gagal mengambil daftar order", e);
        }
        return orders;
    }

    @Override
    public void updateStatus(int orderId, String newStatus) throws RepositoryException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Gagal update status order id " + orderId, e);
        }
    }
}
