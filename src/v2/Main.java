package v2;

import javax.swing.*;

// new Full Screen GUI
public class Main {
	public static void main(String[] args) {
		LogManager log = new LogManager();
		DatabaseConnection dbConnect = new DatabaseConnection();
		SwingUtilities.invokeLater(() -> new DBLoginGUI(dbConnect, log).setVisible(true));
	}
}