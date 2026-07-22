package main.java.tubes.repositories;

import main.java.tubes.exceptions.RepositoryException;
import main.java.tubes.models.Category;
import main.java.tubes.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final RowMapper<Category> mapper = rs -> new Category(rs.getInt("id"), rs.getString("name"));

    @Override
    public List<Category> findAll() throws RepositoryException {
        String sql = "SELECT * FROM categories ORDER BY name ASC";
        List<Category> result = new ArrayList<>();
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) result.add(mapper.map(rs));
        } catch (SQLException e) {
            throw new RepositoryException("Gagal mengambil daftar kategori", e);
        }
        return result;
    }

    @Override
    public Optional<Category> findById(Integer id) throws RepositoryException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapper.map(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Gagal mengambil kategori id " + id, e);
        }
    }

    @Override
    public Category save(Category entity) throws RepositoryException {
        String sql = "INSERT INTO categories (name) VALUES (?) RETURNING id";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) entity.setId(rs.getInt("id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new RepositoryException("Gagal menambah kategori (nama mungkin sudah dipakai)", e);
        }
    }

    @Override
    public void update(Category entity) throws RepositoryException {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Gagal mengubah kategori id " + entity.getId(), e);
        }
    }

    @Override
    public void deleteById(Integer id) throws RepositoryException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Gagal menghapus kategori id " + id, e);
        }
    }
}
