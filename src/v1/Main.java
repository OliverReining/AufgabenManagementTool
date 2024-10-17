package v1;

import javax.swing.SwingUtilities;

//// Admin-GUI
public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new AdminGUI().setVisible(true)); // Öffnet die Admin-GUI
	}

// User-GUI
//	public static void main(String[] args) {
//		User user = new User("olrei", "Reining", "Oliver", "oreining@email.com", "Passwort", "admin");
//		SwingUtilities.invokeLater(() -> new UserGUI(user).setVisible(true)); // Öffnet die User-GUI
//	}

// Database Login Screen
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(() -> new DBLoginGUI_v2().setVisible(true));
//	}

// User Login Screen
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new UserLogin().setVisible(true));
//    }

}