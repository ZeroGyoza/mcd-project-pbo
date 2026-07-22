package main.java.tubes.utils;

import main.java.tubes.models.Account;

public class Session {
    private static Session instance;
    private Account currentAccount;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    public void login(Account account) { this.currentAccount = account; }
    public void logout() { this.currentAccount = null; }
    public Account getCurrentAccount() { return currentAccount; }
    public boolean isLoggedIn() { return currentAccount != null; }
}
