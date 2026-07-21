package main.java.tubes.model;
import main.java.tubes.view.KiosView;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KiosView kiosk = new KiosView();
            kiosk.setVisible(true);
        });
    }
}