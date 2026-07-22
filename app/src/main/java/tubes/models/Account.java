package main.java.tubes.models;

import main.java.tubes.enums.AccountRole;

public class Account {
    private int id;
    private String username;
    private String password; // sudah dalam bentuk hash, jangan simpan plain text
    private AccountRole role;

    public Account() {}

    public Account(int id, String username, String password, AccountRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public AccountRole getRole() { return role; }
    public void setRole(AccountRole role) { this.role = role; }
}
