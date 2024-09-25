package v1;

import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class UserGUI extends JFrame {

	static int userId;
	static Object[] userData;

	public UserGUI(int userId) {
		UserGUI.userId = userId;
		UserGUI.userData = Analytics.showUserData(userId);

		setTitle("Benutzerübersicht");
		setSize(530, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();

		// Benutzer Übersicht (name, vorname, email, userid, passwort ändern...)
		JPanel userOverview = userOverviewPanel();
		tabbedPane.addTab("Übersicht", userOverview);

		// Übersicht über die Projekte an denen der User mitarbeitet
		JPanel projectOverwiev = projectOverwievPanel();
		tabbedPane.addTab("Projekte", projectOverwiev);

		// Übersicht der einzelnen Aufgaben
		JPanel TasksOverview = tasksOverviewPanel();
		tabbedPane.addTab("Aufgaben", TasksOverview);

		add(tabbedPane, BorderLayout.CENTER);

	}

	private JPanel userOverviewPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setBounds(20, 20, 150, 25);
		panel.add(nameLabel);

		JLabel nameLabel2 = new JLabel(userData[1].toString());
		nameLabel2.setBounds(150, 20, 150, 25);
		panel.add(nameLabel2);

		JLabel vornameLabel = new JLabel("Vorname:");
		vornameLabel.setBounds(20, 40, 150, 25);
		panel.add(vornameLabel);

		JLabel vornameLabel2 = new JLabel(userData[2].toString());
		vornameLabel2.setBounds(150, 40, 150, 25);
		panel.add(vornameLabel2);

		JLabel emailLabel = new JLabel("E-Mail:");
		emailLabel.setBounds(20, 60, 150, 25);
		panel.add(emailLabel);

		JLabel emailLabel2 = new JLabel(userData[3].toString());
		emailLabel2.setBounds(150, 60, 150, 25);
		panel.add(emailLabel2);

		JLabel projectsLabel = new JLabel("Aktuelle Projekte:");
		projectsLabel.setBounds(20, 80, 150, 25);
		panel.add(projectsLabel);

		JLabel projectsLabel2 = new JLabel(String.valueOf(Analytics.getProjectCount(userId)));
		projectsLabel2.setBounds(150, 80, 150, 25);
		panel.add(projectsLabel2);

		JLabel taskLabel = new JLabel("Aktuelle Aufgaben:");
		taskLabel.setBounds(20, 100, 150, 25);
		panel.add(taskLabel);

		JLabel taskLabel2 = new JLabel(String.valueOf(Analytics.getTaskCount(userId)));
		taskLabel2.setBounds(150, 100, 150, 25);
		panel.add(taskLabel2);

		return panel;
	}

	private JPanel projectOverwievPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		// TODO Auto-generated method stub
		return panel;
	}

	private JPanel tasksOverviewPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		// TODO Auto-generated method stub
		return panel;
	}

}
