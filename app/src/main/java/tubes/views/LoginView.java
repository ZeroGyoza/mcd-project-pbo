package main.java.tubes.views;

import main.java.tubes.controllers.AuthController;
import main.java.tubes.enums.AccountRole;
import main.java.tubes.exceptions.AuthenticationException;
import main.java.tubes.models.Account;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final AuthController authController = new AuthController();
    private JTextField fieldUsername;
    private JPasswordField fieldPassword;

    public LoginView() {
        setTitle("Login Staff - McDonald's Kiosk");
        setSize(360, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login Admin / Cashier", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        panel.add(new JLabel("Username"), gbc);
        fieldUsername = new JTextField(15);
        gbc.gridx = 1;
        panel.add(fieldUsername, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Password"), gbc);
        fieldPassword = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(fieldPassword, gbc);

        JButton btnLogin = new JButton("Login");
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> doLogin());
        fieldPassword.addActionListener(e -> doLogin());

        add(panel);
    }

    private void doLogin() {
        String username = fieldUsername.getText().trim();
        String password = new String(fieldPassword.getPassword());

        try {
            Account account = authController.login(username, password);
            JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + account.getRole());

            if (account.getRole() == AccountRole.ADMIN) {
                new AdminView().setVisible(true);
            } else {
                new KiosView().setVisible(true); // cashier pakai tampilan kiosk yang sudah ada
            }
            dispose();
        } catch (AuthenticationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Gagal", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
