package v1;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminGUI().setVisible(true);  // Öffnet die GUI
            }
        });
    }
}


//public class Main {
//
//    public static void main(String[] args) {
//        // Zeigt den Login-Screen an
//        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
//    }
//}


// Ändere class um einzelne teile zu prüfen

//public class Main {
//	
//	public static void main(String[] args) {
//        // Zeigt den Login-Screen an
//        SwingUtilities.invokeLater(() -> new UserLogin().setVisible(true));
//    }
//}