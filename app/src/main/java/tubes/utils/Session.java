package main.java.tubes.utils;
import main.java.tubes.models.User;

public class Session {
    private static Session instance;
    private User currentUser;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void login(User user) { this.currentUser = user; }
    public void logout() { this.currentUser = null; }
    public User getCurrentUser() { return currentUser; }
    public boolean isLoggedIn() { return currentUser != null; }
}