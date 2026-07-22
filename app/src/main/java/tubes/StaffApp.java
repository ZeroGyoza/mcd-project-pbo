package main.java.tubes;

import javax.swing.SwingUtilities;
import main.java.tubes.views.LoginView;

// Entry point terpisah buat staff (admin/cashier). App.java yang lama
// (kiosk customer, tanpa login) TIDAK diubah sama sekali.
public class StaffApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
