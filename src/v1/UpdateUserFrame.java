package v1;

import javax.swing.*;
import java.sql.*;

@SuppressWarnings("serial")
public class UpdateUserFrame extends JFrame {

	private UserManager userManager = new UserManager();
	private JTextField nameField;
	private JTextField vornameField;
	private JTextField emailField;
	private JTextField passwordField;
	private JTextField roleField;
	private int userId;

	private void loadUserData(int userId) {
		String sql = "SELECT name, vorname, email, pass, role FROM benutzer WHERE userid = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				// Vorhandene Benutzerdaten in die Textfelder laden
				nameField.setText(rs.getString("name"));
				vornameField.setText(rs.getString("vorname"));
				emailField.setText(rs.getString("email"));
				passwordField.setText(rs.getString("pass"));
				roleField.setText(rs.getString("role"));
			} else {
				JOptionPane.showMessageDialog(this, "Benutzer nicht gefunden!", "Fehler", JOptionPane.ERROR_MESSAGE);
				dispose(); // Schließt das Fenster, wenn der Benutzer nicht existiert
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Datenbankfehler: " + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
			dispose();
		}
	}

	public UpdateUserFrame(int userId) {
		this.userId = userId;
		setTitle("Benutzer ändern");
		setSize(360, 300);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setBounds(20, 20, 100, 25);
		add(nameLabel);

		nameField = new JTextField();
		nameField.setBounds(120, 20, 210, 25);
		add(nameField);

		JLabel vornameLabel = new JLabel("Vorname:");
		vornameLabel.setBounds(20, 60, 100, 25);
		add(vornameLabel);

		vornameField = new JTextField();
		vornameField.setBounds(120, 60, 210, 25);
		add(vornameField);

		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setBounds(20, 100, 100, 25);
		add(emailLabel);

		emailField = new JTextField();
		emailField.setBounds(120, 100, 210, 25);
		add(emailField);

		JLabel passwordLabel = new JLabel("Passwort:");
		passwordLabel.setBounds(20, 140, 100, 25);
		add(passwordLabel);

		passwordField = new JTextField();
		passwordField.setBounds(120, 140, 210, 25);
		add(passwordField);

		JLabel roleLabel = new JLabel("Rolle:");
		roleLabel.setBounds(20, 180, 100, 25);
		add(roleLabel);

		roleField = new JTextField();
		roleField.setBounds(120, 180, 210, 25);
		add(roleField);

		JButton updateButton = new JButton("Speichern");
		updateButton.setBounds(20, 220, 150, 30);
		updateButton.addActionListener(e -> updateUser());
		add(updateButton);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.setBounds(180, 220, 150, 30);
		cancelButton.addActionListener(e -> System.exit(0));
		add(cancelButton);

		// Benutzerdaten laden und in die Textfelder einfügen
		loadUserData(userId);
	}

	private void updateUser() {
		String newName = nameField.getText();
		String newVorname = vornameField.getText();
		String newEmail = emailField.getText();
		String password = passwordField.getText();
		String role = roleField.getText();

		userManager.updateUser(userId, newName, newVorname, newEmail, password, role);
	}
}
