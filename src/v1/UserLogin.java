package v1;

import java.sql.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class UserLogin extends JFrame {

	private JTextField usernameField;
	private JPasswordField passwordField;

	public UserLogin() {
		setTitle("Login");
		setSize(300, 180);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		JLabel usernameLabel = new JLabel("Email:");
		usernameLabel.setBounds(20, 20, 100, 25);
		add(usernameLabel);

		usernameField = new JTextField();
		usernameField.setBounds(120, 20, 150, 25);
		add(usernameField);

		JLabel passwordLabel = new JLabel("Passwort:");
		passwordLabel.setBounds(20, 60, 100, 25);
		add(passwordLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(120, 60, 150, 25);
		add(passwordField);

		JButton connectButton = new JButton("Verbinden");
		connectButton.setBounds(20, 100, 250, 30);
		connectButton.addActionListener(e -> {connect();

		});
		add(connectButton);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.addActionListener(e -> System.exit(0));
		add(cancelButton);

	}

	public void connect() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		String sql = "SELECT pass, role, userid FROM benutzer WHERE email = ?";
		try {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int userId = rs.getInt("userid");
				String storedPass = rs.getString("pass");
				String role = rs.getString("role");
				if (storedPass.equals(password) && role.equals("admin")) {
					dispose();
					new AdminGUI().setVisible(true);
				} else if (storedPass.equals(password) && role.equals("user")) {
					dispose();
					new UserGUI(userId).setVisible(true);
				}
			}
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(this, "Verbindung fehlgeschlagen" + e1.getMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}

}
