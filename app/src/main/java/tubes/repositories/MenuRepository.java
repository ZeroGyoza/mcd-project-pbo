package main.java.tubes.repositories;
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
}