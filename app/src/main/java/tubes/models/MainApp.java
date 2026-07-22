package main.java.tubes.models;
import javax.swing.SwingUtilities;

import main.java.tubes.views.KiosView;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KiosView kiosk = new KiosView();
            kiosk.setVisible(true);
        });
    }
}