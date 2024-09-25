package v1;

import javax.swing.*;

//// Admin-GUI
//public class Main {
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(() -> new AdminGUI().setVisible(true)); // Öffnet die Admin-GUI
//	}
//}

// User-GUI
public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new UserGUI(2).setVisible(true)); // Öffnet die User-GUI
	}
}

// Login Screen
//public class Main {
//    public static void main(String[] args) {
//        // Zeigt den Login-Screen an
//        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
//    }
//}

// Ändere class um einzelne teile zu prüfen
//public class Main {	
//	public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new UserLogin().setVisible(true));
//    }
//}