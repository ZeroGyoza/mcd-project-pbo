package main.java.tubes.repositories;

import main.java.tubes.models.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository {
    User save(User user) throws Exception;
    Optional<User> findByEmail(String email) throws Exception;
    Optional<User> findById(String id) throws Exception;
    boolean emailExists(String email) throws Exception;
    void updatePassword(String id, String hashedPassword) throws Exception;
    void updateResetToken(String id, String token, LocalDateTime expires) throws Exception;
    Optional<User> findByResetToken(String token) throws Exception;
    void clearResetToken(String id) throws Exception;
}