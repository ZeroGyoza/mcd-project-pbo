package main.java.tubes.repositories;

import main.java.tubes.enums.AccountRole;
import main.java.tubes.exceptions.RepositoryException;
import main.java.tubes.models.Account;
import main.java.tubes.utils.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    // RowMapper = cara mapping 1 baris tabel "accounts" jadi objek Account (konsep ORM sederhana)
    private final RowMapper<Account> mapper = rs -> new Account(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            AccountRole.valueOf(rs.getString("role").toUpperCase())
    );

    @Override
    public List<Account> findAll() throws RepositoryException {
        String sql = "SELECT * FROM accounts ORDER BY id ASC";
        List<Account> result = new ArrayList<>();
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) result.add(mapper.map(rs));
        } catch (SQLException e) {
            throw new RepositoryException("Gagal mengambil daftar akun", e);
        }
        return result;
    }

    @Override
    public Optional<Account> findById(Integer id) throws RepositoryException {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapper.map(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Gagal mengambil akun id " + id, e);
        }
    }

    @Override
    public Optional<Account> findByUsername(String username) throws RepositoryException {
        String sql = "SELECT * FROM accounts WHERE username = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapper.map(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Gagal mengambil akun username " + username, e);
        }
    }

    @Override
    public Account save(Account entity) throws RepositoryException {
        String sql = "INSERT INTO accounts (username, password, role) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getRole().name().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) entity.setId(rs.getInt("id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new RepositoryException("Gagal menyimpan akun baru", e);
        }
    }

    @Override
    public void update(Account entity) throws RepositoryException {
        String sql = "UPDATE accounts SET username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getRole().name().toLowerCase());
            ps.setInt(4, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Gagal mengubah akun id " + entity.getId(), e);
        }
    }

    @Override
    public void deleteById(Integer id) throws RepositoryException {
        String sql = "DELETE FROM accounts WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Gagal menghapus akun id " + id, e);
        }
    }
}
