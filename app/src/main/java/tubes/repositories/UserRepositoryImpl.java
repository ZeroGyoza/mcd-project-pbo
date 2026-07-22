package main.java.tubes.repositories;

import main.java.tubes.models.Role;
import main.java.tubes.models.User;
import main.java.tubes.utils.Database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User save(User user) throws Exception {
        String sql = "INSERT INTO users (id, name, email, password, address, birth_of_date, salary, role, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now(), now())";

        String id = UUID.randomUUID().toString();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(id));
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getAddress());
            if (user.getBirthOfDate() != null) {
                ps.setDate(6, Date.valueOf(user.getBirthOfDate()));
            } else {
                ps.setNull(6, Types.DATE);
            }
            if (user.getSalary() != null) {
                ps.setDouble(7, user.getSalary());
            } else {
                ps.setNull(7, Types.NUMERIC);
            }
            ps.setString(8, user.getRole().toDbValue());

            ps.executeUpdate();
            user.setId(id);
            return user;
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM users WHERE email = ? AND deleted_at IS NULL";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<User> findById(String id) throws Exception {
        String sql = "SELECT * FROM users WHERE id = ? AND deleted_at IS NULL";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, UUID.fromString(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public boolean emailExists(String email) throws Exception {
        return findByEmail(email).isPresent();
    }

    @Override
    public void updatePassword(String id, String hashedPassword) throws Exception {
        String sql = "UPDATE users SET password = ?, updated_at = now() WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setObject(2, UUID.fromString(id));
            ps.executeUpdate();
        }
    }

    @Override
    public void updateResetToken(String id, String token, LocalDateTime expires) throws Exception {
        String sql = "UPDATE users SET reset_password_token = ?, reset_password_expires = ?, updated_at = now() WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setTimestamp(2, Timestamp.valueOf(expires));
            ps.setObject(3, UUID.fromString(id));
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<User> findByResetToken(String token) throws Exception {
        String sql = "SELECT * FROM users WHERE reset_password_token = ? AND deleted_at IS NULL";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public void clearResetToken(String id) throws Exception {
        String sql = "UPDATE users SET reset_password_token = NULL, reset_password_expires = NULL, updated_at = now() WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, UUID.fromString(id));
            ps.executeUpdate();
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setAddress(rs.getString("address"));

        Date bod = rs.getDate("birth_of_date");
        if (bod != null) user.setBirthOfDate(bod.toLocalDate());

        double salary = rs.getDouble("salary");
        if (!rs.wasNull()) user.setSalary(salary);

        user.setRole(Role.fromDbValue(rs.getString("role")));
        user.setResetPasswordToken(rs.getString("reset_password_token"));

        Timestamp expires = rs.getTimestamp("reset_password_expires");
        if (expires != null) user.setResetPasswordExpires(expires.toLocalDateTime());

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) user.setCreatedAt(createdAt.toLocalDateTime());

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) user.setUpdatedAt(updatedAt.toLocalDateTime());

        return user;
    }
}