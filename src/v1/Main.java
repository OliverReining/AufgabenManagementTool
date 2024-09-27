package v1;

import javax.swing.*;

//// Admin-GUI
//public class Main {
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(() -> new AdminGUI().setVisible(true)); // Öffnet die Admin-GUI
//	}
//}

// User-GUI
//public class Main {
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(() -> new UserGUI(5).setVisible(true)); // Öffnet die User-GUI
//	}
//}

// Database Login Screen
//public class Main {
//	public static void main(String[] args) {
//		// Zeigt den Login-Screen an
//		SwingUtilities.invokeLater(() -> new DBLogin().setVisible(true));
//	}
//}

// User Login Screen
public class Main {	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserLogin().setVisible(true));
    }
}