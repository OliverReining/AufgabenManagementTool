package v2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class LogManager {

	private JTextArea displayArea; // Kann null sein, wenn keine Anzeige existiert
	private ArrayList<Log> logMessages;

	public LogManager() {
		this.logMessages = new ArrayList<>();
	}

	// Nachricht loggen
	public void log(String message, Log.LogType type) {
		Log log = new Log(message, type);
		logMessages.add(log);

		if (displayArea != null) {
			appendToDisplayArea(log);
		}
	}

	public void sqlExceptionLog(SQLException e, String sql) {
		Log log = new Log(sql + " konnte nicht ausgeführt werden\n" + e.getErrorCode() + "\n" + e.getMessage(),
				Log.LogType.ERROR);
		logMessages.add(log);

		if (displayArea != null) {
			appendToDisplayArea(log);
		}
	}

	public void successLog(String event) {
		Log log = new Log(event + " erfolgreich", Log.LogType.SUCCESS);
		logMessages.add(log);

		if (displayArea != null) {
			appendToDisplayArea(log);
		}
	}

	public void notFoundLog(String item) {
		Log log = new Log(item + " nicht gefunden.", Log.LogType.ERROR);
		logMessages.add(log);

		if (displayArea != null) {
			appendToDisplayArea(log);
		}

	}

	private void appendToDisplayArea(Log log) {
		// Log zur JTextArea hinzufügen
		displayArea.append(log.toString() + "\n");

		// Automatisch zum Ende der JTextArea scrollen
		displayArea.setCaretPosition(displayArea.getDocument().getLength());
	}

	// Logs als String ausgeben für Weiterverarbeitung und Speicherung in txt oder
	// so
	public String getLogsAsString() {
		StringBuilder logs = new StringBuilder();
		for (Log log : logMessages) {
			logs.append(log.toString()).append("\n");
		}
		return logs.toString();
	}

	// Optionales Setzen der displayArea, kann auch später aufgerufen werden
	public void setDisplayArea(JTextArea displayArea) {
		this.displayArea = displayArea;
		updateDisplayArea(); // Falls bereits Logs existieren, zeige diese sofort an
	}

	// DisplayArea aktualisieren, falls sie existiert
	private void updateDisplayArea() {
		if (displayArea != null) {
			displayArea.setText(getLogsAsString());
			displayArea.revalidate();
			displayArea.repaint();
		}
	}

	// Save Logs Methode
	public void saveLogs() {
		String directory = "log"; // Ordner für die Dateien
		String baseFileName = "log"; // Standart-Name
		String fileExtension = ".txt"; // Dateiformat

		String finalFileName = getUniqueFileName(directory, baseFileName, fileExtension);

		try {
			Path filePath = Paths.get(directory, finalFileName);
			Files.write(filePath, getLogsAsString().getBytes(), StandardOpenOption.CREATE);
			JOptionPane.showMessageDialog(null, "Log gespeichert: " + filePath.toString());
		} catch (IOException e) {
			log("Log konnte nicht gespeichert", Log.LogType.ERROR);
			log(e.getMessage(), Log.LogType.INFO);
		}

	}

	private static String getUniqueFileName(String directory, String baseFileName, String fileExtension) {
		int counter = 1;
		String fileName = baseFileName + fileExtension;
		File file = new File(directory, fileName);

		// Solange die Datei existiert, füge eine fortlaufende Nummer hinzu
		while (file.exists()) {
			String formattedNumber = String.format("%04d", counter); // Formatierung führende Nullen (0001, 0002, ...)
			fileName = baseFileName + "_" + formattedNumber + fileExtension;
			file = new File(directory, fileName);
			counter++;
		}

		return fileName;
	}

}
