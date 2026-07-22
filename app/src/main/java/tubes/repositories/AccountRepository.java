package main.java.tubes.repositories;

import main.java.tubes.exceptions.RepositoryException;
import main.java.tubes.models.Account;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByUsername(String username) throws RepositoryException;
}
