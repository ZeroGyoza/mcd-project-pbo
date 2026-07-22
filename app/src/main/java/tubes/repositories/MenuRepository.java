package main.java.tubes.repositories;
import main.java.tubes.exceptions.RepositoryException;
import main.java.tubes.models.*;
import main.java.tubes.utils.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuRepository {
    public List<Menu> getAllMenu() {
        List<Menu> listMenu = new ArrayList<>();
        String query = "SELECT id, name, price, type, sub_category, image_url FROM menus ORDER BY id ASC";

        try (Connection conn = Database.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String typeStr = rs.getString("type");
                String subCat = rs.getString("sub_category");
                String imgUrl = rs.getString("image_url");

                if (typeStr.equalsIgnoreCase("DRINK")) {
                    listMenu.add(new Drink(id, name, price, subCat, imgUrl));
                } else if (typeStr.equalsIgnoreCase("PAKET")) {
                    listMenu.add(new Paket(id, name, price, subCat, imgUrl));
                } else {
                    listMenu.add(new Food(id, name, price, subCat, imgUrl));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listMenu;
    }

    // ==================== Bagian Admin: CRUD Menu ====================

    public void addMenu(String name, double price, String type, String subCategory, String imageUrl) throws RepositoryException {
        String sql = "INSERT INTO menus (name, price, type, sub_category, image_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setString(3, type);
            ps.setString(4, subCategory);
            ps.setString(5, imageUrl);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Gagal menambah menu baru", e);
        }
    }

    public void updateMenu(int id, String name, double price, String subCategory, String imageUrl) throws RepositoryException {
        String sql = "UPDATE menus SET name = ?, price = ?, sub_category = ?, image_url = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setString(3, subCategory);
            ps.setString(4, imageUrl);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Gagal mengubah menu id " + id, e);
        }
    }

    public void deleteMenu(int id) throws RepositoryException {
        String sql = "DELETE FROM menus WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Gagal menghapus menu id " + id, e);
        }
    }
}