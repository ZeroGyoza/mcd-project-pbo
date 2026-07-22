package main.java.tubes.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface generic + functional interface.
 * Tugasnya cuma satu: "mapping" 1 baris ResultSet (dari SQL) jadi 1 objek Java.
 * Ini konsepnya sama kayak ORM (Object-Relational Mapping) beneran (Hibernate, dll),
 * cuma versi manual/simpel biar gampang dijelasin: baris tabel -> objek.
 */
@FunctionalInterface
public interface RowMapper<T> {
    T map(ResultSet rs) throws SQLException;
}
