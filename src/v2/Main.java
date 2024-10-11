package v2;

import javax.swing.*;

// new Full Screen GUI
public class Main {
	public static void main(String[] args) {
		// globaler LogManager für einen kompletten Log initialisieren
		LogManager log = new LogManager();
		// globale Connection, um nicht für jedes Statement neue Connection aufzubauen
		// benutzt LogManager
		DatabaseConnection dbConnect = new DatabaseConnection(log);
		// neue Login-GUI mit dbConnect und LogManager starten
		SwingUtilities.invokeLater(() -> new DBLoginGUI(dbConnect, log).setVisible(true));
	}
}