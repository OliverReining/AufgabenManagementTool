package v1;

import javax.swing.*;

import java.awt.*;
import java.sql.*;

@SuppressWarnings("serial")
public class DBLoginGUI_v1 extends JFrame {

    private JTextField dbNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public DBLoginGUI_v1() {
        setTitle("Datenbankverbindung");
        setSize(250, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel dbNameLabel = new JLabel("Datenbankname:");
        dbNameField = new JTextField();
        add(dbNameLabel);
        add(dbNameField);

        JLabel usernameLabel = new JLabel("Benutzername:");
        usernameField = new JTextField();
        add(usernameLabel);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Passwort:");
        passwordField = new JPasswordField();
        add(passwordLabel);
        add(passwordField);

        JButton connectButton = new JButton("Verbinden");
        connectButton.addActionListener(e -> connectToDatabase());
        add(connectButton);

        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(e -> System.exit(0));
        add(cancelButton);
    }

    private void connectToDatabase() {
        String databaseName = dbNameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Überprüfen, ob die erforderlichen Felder ausgefüllt sind
        if (databaseName.isEmpty() || username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Datenbankname und Benutzername dürfen nicht leer sein.", "Fehler", JOptionPane.ERROR_MESSAGE); // "this" zeigt Fehlermeldung zentriert in GUI an
            return;
        }

        try {
            // Verbindungsversuch mit den Benutzereingaben
			DatabaseConnection_v1.getConnection(databaseName, username, password);

            // Wenn die Verbindung erfolgreich ist, werden die Standardwerte gesetzt
            DatabaseConnection_v1.setCredentials(databaseName, username, password);
            
            // Erfolgsmeldung anzeigen
            JOptionPane.showMessageDialog(this, "Erfolgreich verbunden!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            
            // Schließen des Login-Fensters und Öffnen der Hauptanwendung
            dispose();  // Schließt das Login-Fenster
//          new AdminGUI().setVisible(true);  // Öffnet die Hauptanwendung
//            new UserLogin().setVisible(true); 
            
        } catch (SQLException e) {
            // Fehlermeldung anzeigen, wenn die Verbindung fehlschlägt
            JOptionPane.showMessageDialog(this, "Verbindung fehlgeschlagen: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
