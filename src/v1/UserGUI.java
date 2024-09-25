package v1;

import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class UserGUI extends JFrame {
	
	public UserGUI(int userId) {
		setTitle("");
		setSize(530, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();

		// Benutzer Übersicht (name, vorname, email, userid, passwort ändern...)
		JPanel userOverview = userOverviewPanel(userId);
		tabbedPane.addTab("Übersicht", userOverview);

		// Übersicht über die Projekte an denen der User mitarbeitet
		JPanel projectOverwiev = projectOverwievPanel(userId);
		tabbedPane.addTab("Projekte", projectOverwiev);

		// Übersicht der einzelnen Aufgaben
		JPanel TasksOverview = tasksOverviewPanel(userId);
		tabbedPane.addTab("Aufgaben", TasksOverview);

		add(tabbedPane, BorderLayout.CENTER);

	}

	private JPanel userOverviewPanel(int userId) {
		Object[] userData = Analytics.showUserData(userId);
		JPanel panel = new JPanel();
		panel.setLayout(null);

		JLabel nameLabel = new JLabel("Name: " + userData[1].toString());
		nameLabel.setBounds(20, 20, 250, 25);
		panel.add(nameLabel);

		return panel;
	}

	private JPanel projectOverwievPanel(int userId) {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		// TODO Auto-generated method stub
		return panel;
	}

	private JPanel tasksOverviewPanel(int userId) {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		// TODO Auto-generated method stub
		return panel;
	}

}
