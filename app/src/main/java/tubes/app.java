package main.java.tubes;
import javax.swing.SwingUtilities;

import main.java.tubes.views.KiosView;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KiosView kiosk = new KiosView();
            kiosk.setVisible(true);
        }); 
    }
}