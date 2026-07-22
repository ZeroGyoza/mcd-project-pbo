package main.java.tubes.models;

import main.java.tubes.utils.Database;
import main.java.tubes.models.CartItem;

import java.sql.*;

public class CartDAO {

    // Ambil Cart ID yang aktif, atau buat baru di PostgreSQL
    public int getOrCreateActiveCartId() {
        int cartId = -1;
        String selectSql = "SELECT id FROM carts WHERE status = 'ACTIVE' ORDER BY id DESC LIMIT 1";
        String insertSql = "INSERT INTO carts (created_at, status) VALUES (CURRENT_TIMESTAMP, 'ACTIVE') RETURNING id";

        try (Connection conn = Database.connect()) {
            if (conn == null) return -1;

            PreparedStatement ps = conn.prepareStatement(selectSql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cartId = rs.getInt("id");
            } else {
                PreparedStatement psInsert = conn.prepareStatement(insertSql);
                ResultSet rsKey = psInsert.executeQuery();
                if (rsKey.next()) {
                    cartId = rsKey.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Gagal mendapatkan/membuat cart di PostgreSQL!");
            e.printStackTrace();
        }
        return cartId;
    }

    // Tambah atau Update Item di cart_items
    public void addOrUpdateItem(int cartId, int menuId, int quantity, double price) {
        String checkSql = "SELECT quantity FROM cart_items WHERE cart_id = ? AND menu_id = ?";
        String updateSql = "UPDATE cart_items SET quantity = quantity + ?, total_price = (quantity + ?) * ? WHERE cart_id = ? AND menu_id = ?";
        String insertSql = "INSERT INTO cart_items (cart_id, menu_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect()) {
            if (conn == null) return;

            PreparedStatement psCheck = conn.prepareStatement(checkSql);
            psCheck.setInt(1, cartId);
            psCheck.setInt(2, menuId);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                PreparedStatement psUpdate = conn.prepareStatement(updateSql);
                psUpdate.setInt(1, quantity);
                psUpdate.setInt(2, quantity);
                psUpdate.setDouble(3, price);
                psUpdate.setInt(4, cartId);
                psUpdate.setInt(5, menuId);
                psUpdate.executeUpdate();
            } else {
                PreparedStatement psInsert = conn.prepareStatement(insertSql);
                psInsert.setInt(1, cartId);
                psInsert.setInt(2, menuId);
                psInsert.setInt(3, quantity);
                psInsert.setDouble(4, price);
                psInsert.setDouble(5, price * quantity);
                psInsert.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("❌ Gagal menambah/update item di cart_items!");
            e.printStackTrace();
        }
    }

    // Update Quantity Spesifik (saat tombol + / - diklik)
    public void updateQuantity(int cartId, int menuId, int newQuantity, double price) {
        String updateSql = "UPDATE cart_items SET quantity = ?, total_price = ? WHERE cart_id = ? AND menu_id = ?";
        try (Connection conn = Database.connect()) {
            if (conn == null) return;

            PreparedStatement ps = conn.prepareStatement(updateSql);
            ps.setInt(1, newQuantity);
            ps.setDouble(2, price * newQuantity);
            ps.setInt(3, cartId);
            ps.setInt(4, menuId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Gagal update quantity cart!");
            e.printStackTrace();
        }
    }

    // Hapus satu item dari cart_items
    public void removeItem(int cartId, int menuId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ? AND menu_id = ?";
        try (Connection conn = Database.connect()) {
            if (conn == null) return;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);
            ps.setInt(2, menuId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Gagal menghapus item dari cart_items!");
            e.printStackTrace();
        }
    }

    // Kosongkan keranjang saat Checkout selesai
    public void clearCart(int cartId) {
        String deleteItems = "DELETE FROM cart_items WHERE cart_id = ?";
        String updateCart = "UPDATE carts SET status = 'COMPLETED' WHERE id = ?";
        
        try (Connection conn = Database.connect()) {
            if (conn == null) return;

            PreparedStatement ps1 = conn.prepareStatement(deleteItems);
            ps1.setInt(1, cartId);
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(updateCart);
            ps2.setInt(1, cartId);
            ps2.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Gagal mengosongkan cart!");
            e.printStackTrace();
        }
    }
}