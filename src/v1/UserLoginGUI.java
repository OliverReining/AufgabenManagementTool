package v1;

import java.sql.*;
import javax.swing.*;

import v2.DatabaseConnection;

public class UserLoginGUI extends JFrame {

	private JTextField emailField;
	private JPasswordField passwordField;
	private DatabaseConnection dbConnect;

	public UserLoginGUI(DatabaseConnection dbConnect) {
		this.dbConnect = dbConnect;
		setTitle("Login");
		setSize(300, 180);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setBounds(20, 20, 100, 25);
		add(emailLabel);

		emailField = new JTextField();
		emailField.setBounds(120, 20, 150, 25);
		add(emailField);

		JLabel passwordLabel = new JLabel("Passwort:");
		passwordLabel.setBounds(20, 60, 100, 25);
		add(passwordLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(120, 60, 150, 25);
		add(passwordField);

		JButton connectButton = new JButton("Verbinden");
		connectButton.setBounds(20, 100, 250, 30);
		connectButton.addActionListener(e -> {
			connect();

		});
		add(connectButton);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.addActionListener(e -> System.exit(0));
		add(cancelButton);

	}

	public void connect() {
		String email = emailField.getText();
		String password = new String(passwordField.getPassword());
		String sql = "SELECT * FROM benutzer WHERE email = ?";
		try {
			PreparedStatement stmt = dbConnect.getConnection().prepareStatement(sql);
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String userId = rs.getString("userid");
				String name = rs.getString("name");
				String vorname = rs.getString("vorname");
				String storedPass = rs.getString("pass");
				String role = rs.getString("role");

//				User currentUser = new User(userId, name, vorname, email, storedPass, role);

				if (storedPass.equals(password) && role.equals("admin")) {
					dispose();
//					new AdminModeFrame(currentUser).setVisible(true);
				} else if (storedPass.equals(password) && role.equals("user")) {
					dispose();
//					new FullScreenGUI().setVisible(true); // TODO: Connection und currentUser übergeben übergeben
//					new UserGUI(currentUser).setVisible(true);
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Verbindung fehlgeschlagen" + e.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}

	}

}
