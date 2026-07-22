package main.java.tubes.controllers;

import main.java.tubes.exceptions.AuthenticationException;
import main.java.tubes.exceptions.RepositoryException;
import main.java.tubes.models.Account;
import main.java.tubes.repositories.AccountRepository;
import main.java.tubes.repositories.AccountRepositoryImpl;
import main.java.tubes.utils.PasswordUtil;
import main.java.tubes.utils.Session;

import java.util.Optional;

public class AuthController {
    private final AccountRepository accountRepository;

    public AuthController() {
        this.accountRepository = new AccountRepositoryImpl();
    }

    public Account login(String username, String password) throws AuthenticationException, RepositoryException {
        Optional<Account> found = accountRepository.findByUsername(username);
        if (found.isEmpty()) {
            throw new AuthenticationException("Username tidak ditemukan");
        }
        Account account = found.get();
        if (!PasswordUtil.verify(password, account.getPassword())) {
            throw new AuthenticationException("Password salah");
        }
        Session.getInstance().login(account);
        return account;
    }
}
