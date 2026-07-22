package main.java.tubes.repositories;

import main.java.tubes.utils.Database;
import java.sql.*;

public class CartRepository {

    // 1. Ambil ID cart paling baru, atau buat baru jika belum ada
    public int getOrCreateActiveCartId() {
        int cartId = -1;
        String selectSql = "SELECT id FROM carts ORDER BY id DESC LIMIT 1";
        String insertSql = "INSERT INTO carts (created_at) VALUES (CURRENT_TIMESTAMP) RETURNING id";

        try (Connection conn = Database.connect()) {
            if (conn == null) {
                System.err.println("❌ Koneksi database gagal!");
                return -1;
            }

            PreparedStatement ps = conn.prepareStatement(selectSql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cartId = rs.getInt("id");
                System.out.println("✅ Cart Aktif Ditemukan di DB. ID: " + cartId);
            } else {
                PreparedStatement psInsert = conn.prepareStatement(insertSql);
                ResultSet rsKey = psInsert.executeQuery();
                if (rsKey.next()) {
                    cartId = rsKey.getInt("id");
                    System.out.println("🎉 Cart Pertama Berhasil Dibuat di DB! ID: " + cartId);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ ERROR [getOrCreateActiveCartId]: " + e.getMessage());
            e.printStackTrace();
        }
        return cartId;
    }

    // 2. Tambah / Update Item di cart_items
    public void addOrUpdateItem(int cartId, int menuId, int quantity) {
        if (cartId <= 0) {
            System.err.println("❌ Gagal simpan ke cart_items: Cart ID tidak valid (" + cartId + ")");
            return;
        }

        String checkSql = "SELECT quantity FROM cart_items WHERE cart_id = ? AND menu_id = ?";
        String updateSql = "UPDATE cart_items SET quantity = quantity + ? WHERE cart_id = ? AND menu_id = ?";
        String insertSql = "INSERT INTO cart_items (cart_id, menu_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = Database.connect()) {
            if (conn == null) return;

            PreparedStatement psCheck = conn.prepareStatement(checkSql);
            psCheck.setInt(1, cartId);
            psCheck.setInt(2, menuId);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                PreparedStatement psUpdate = conn.prepareStatement(updateSql);
                psUpdate.setInt(1, quantity);
                psUpdate.setInt(2, cartId);
                psUpdate.setInt(3, menuId);
                int rowsUpdated = psUpdate.executeUpdate();
                System.out.println("🔄 Updated cart_items! Row terpengaruh: " + rowsUpdated);
            } else {
                PreparedStatement psInsert = conn.prepareStatement(insertSql);
                psInsert.setInt(1, cartId);
                psInsert.setInt(2, menuId);
                psInsert.setInt(3, quantity);
                int rowsInserted = psInsert.executeUpdate();
                System.out.println("➕ Inserted item ke cart_items! Row terpengaruh: " + rowsInserted);
            }
        } catch (SQLException e) {
            System.err.println("❌ ERROR [addOrUpdateItem]: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 3. Update Quantity (Tombol + / -)
    public void updateQuantity(int cartId, int menuId, int newQuantity) {
        if (cartId <= 0) return;

        String updateSql = "UPDATE cart_items SET quantity = ? WHERE cart_id = ? AND menu_id = ?";
        try (Connection conn = Database.connect()) {
            if (conn == null) return;

            PreparedStatement ps = conn.prepareStatement(updateSql);
            ps.setInt(1, newQuantity);
            ps.setInt(2, cartId);
            ps.setInt(3, menuId);
            ps.executeUpdate();
            System.out.println("✏️ Updated quantity menu_id " + menuId + " menjadi " + newQuantity);
        } catch (SQLException e) {
            System.err.println("❌ ERROR [updateQuantity]: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 4. Hapus Item dari Cart (Tombol X)
    public void removeItem(int cartId, int menuId) {
        if (cartId <= 0) return;

        String sql = "DELETE FROM cart_items WHERE cart_id = ? AND menu_id = ?";
        try (Connection conn = Database.connect()) {
            if (conn == null) return;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);
            ps.setInt(2, menuId);
            ps.executeUpdate();
            System.out.println("🗑️ Deleted menu_id " + menuId + " dari cart_items");
        } catch (SQLException e) {
            System.err.println("❌ ERROR [removeItem]: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 5. Bersihkan Keranjang saat Checkout Selesai
    public void clearCart(int cartId) {
        if (cartId <= 0) return;

        String deleteCart = "DELETE FROM carts WHERE id = ?";

        try (Connection conn = Database.connect()) {
            if (conn == null) return;

            PreparedStatement ps = conn.prepareStatement(deleteCart);
            ps.setInt(1, cartId);
            ps.executeUpdate();
            System.out.println("🧹 Sesi Cart ID " + cartId + " dan seluruh itemnya berhasil dibersihkan.");
        } catch (SQLException e) {
            System.err.println("❌ ERROR [clearCart]: " + e.getMessage());
            e.printStackTrace();
        }
    }
}