package main.java.tubes.repositories;

import main.java.tubes.exceptions.RepositoryException;

import java.util.List;
import java.util.Optional;

/**
 * Interface generic. T = tipe entity (Category, Account, dst), ID = tipe primary key.
 * Semua repository yang butuh operasi CRUD standar tinggal implements interface ini,
 * jadi kontraknya seragam (Dependency Inversion Principle: controller cukup kenal
 * interface ini, ga perlu tau implementasi konkretnya pakai JDBC/Postgres).
 */
public interface CrudRepository<T, ID> {
    List<T> findAll() throws RepositoryException;
    Optional<T> findById(ID id) throws RepositoryException;
    T save(T entity) throws RepositoryException;
    void update(T entity) throws RepositoryException;
    void deleteById(ID id) throws RepositoryException;
}
