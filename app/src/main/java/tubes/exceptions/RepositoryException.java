package main.java.tubes.exceptions;

/**
 * Custom checked exception. Dilempar setiap kali operasi ke database gagal,
 * supaya error dari SQLException (checked) nggak bocor langsung ke layer atas
 * dan bisa dikasih pesan yang lebih jelas buat user.
 */
public class RepositoryException extends Exception {
    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
