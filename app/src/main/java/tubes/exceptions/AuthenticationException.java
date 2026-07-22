package main.java.tubes.exceptions;

/**
 * Custom checked exception khusus untuk kegagalan proses login
 * (username tidak ditemukan / password salah).
 */
public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}
