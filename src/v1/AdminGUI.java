package v1;

import javax.swing.*;
import javax.swing.table.TableModel;

import java.awt.*;

@SuppressWarnings("serial")
public class AdminGUI extends JFrame {

	private UserManager userManager = new UserManager();
	private ProjectManager projectManager = new ProjectManager();
	private TaskManager taskManager = new TaskManager();

	// Konstruktor der Klasse, initialisiert das GUI-Fenster.
	public AdminGUI() {
		setTitle("Aufgabenmanagement System"); // Fenstertitel.
		setSize(530, 500); // Größe des Fensters (530x500 Pixel).
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Schließaktion (beendet das Programm).
		setLayout(new BorderLayout()); // Layout des Fensters = BorderLayout.
		setLocationRelativeTo(null); // Fenster in der Mitte
		setResizable(false); // nicht veränderbare Größe

		// Erstellt ein TabbedPane, um mehrere Tabs für unterschiedliche
		// Verwaltungspanels hinzuzufügen.
		JTabbedPane tabbedPane = new JTabbedPane();

		// Benutzerverwaltungspanel erstellen und als Tab hinzufügen.
		JPanel userPanel = createUserPanel();
		tabbedPane.addTab("Benutzerverwaltung", userPanel);

		// Projektverwaltungspanel erstellen und als Tab hinzufügen.
		JPanel projectPanel = createProjectPanel();
		tabbedPane.addTab("Projektverwaltung", projectPanel);

		// Aufgabenverwaltungspanel erstellen und als Tab hinzufügen.
		JPanel taskPanel = createTaskPanel();
		tabbedPane.addTab("Aufgabenverwaltung", taskPanel);

		// Fügt das TabbedPane in das Hauptfenster ein.
		add(tabbedPane, BorderLayout.CENTER);
	}

	// Erstellt das Panel für die Benutzerverwaltung.
	private JPanel createUserPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null); // Setzt das Layout auf "null", -> manuelle Positionierung der Komponenten.

		// Label für den Namen.
		JLabel nameLabel = new JLabel("Name:");
		// Position und Größe des Labels. (x-Koordiante, y-Koordinate, Breite, Höhe)
		nameLabel.setBounds(20, 20, 100, 25);
		panel.add(nameLabel);

		// Textfeld für die Eingabe des Namens.
		JTextField nameField = new JTextField();
		nameField.setBounds(120, 20, 210, 25); // Position und Größe.
		panel.add(nameField);

		// Label für den Vornamen.
		JLabel vornameLabel = new JLabel("Vorname:");
		vornameLabel.setBounds(20, 60, 100, 25);
		panel.add(vornameLabel);

		// Textfeld für die Eingabe des Vornamens.
		JTextField vornameField = new JTextField();
		vornameField.setBounds(120, 60, 210, 25);
		panel.add(vornameField);

		// Label für die E-Mail.
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setBounds(20, 100, 100, 25);
		panel.add(emailLabel);

		// Textfeld für die Eingabe der E-Mail.
		JTextField emailField = new JTextField();
		emailField.setBounds(120, 100, 210, 25);
		panel.add(emailField);

		// Textarea mit einem ScrollPane für die Anzeige von Benutzern.
		JTextArea displayArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setBounds(20, 220, 473, 200); // Position und Größe des ScrollPane.
		panel.add(scrollPane);

		// "Benutzer erstellen" Button
		JButton createUserButton = new JButton("Benutzer erstellen");
		createUserButton.setBounds(20, 140, 150, 30);
		createUserButton.addActionListener(e -> {
			String name = nameField.getText();
			String vorname = vornameField.getText();
			String email = emailField.getText();

			// Validierung: Prüfen, ob die Felder leer sind
			if (name.isEmpty() || vorname.isEmpty() || email.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Alle Felder müssen ausgefüllt werden.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				userManager.createUser(name, vorname, email);

				// Textfelder leeren
				nameField.setText("");
				vornameField.setText("");
				emailField.setText("");
			}
		});
		panel.add(createUserButton);

		// Felder Leeren Button
		JButton clearFieldsButton = new JButton("Felder löschen");
		clearFieldsButton.setBounds(340, 20, 150, 25);
		clearFieldsButton.addActionListener(e -> {
			nameField.setText("");
			vornameField.setText("");
			emailField.setText("");
			displayArea.setText("");
		});
		panel.add(clearFieldsButton);

		// Email-Adresse erzeugen
		JButton createEmailButton = new JButton("Email generieren");
		createEmailButton.setBounds(340, 100, 150, 25);
		createEmailButton.addActionListener(e -> {
			String name = nameField.getText();
			String vorname = vornameField.getText();
			if (name.isEmpty() || vorname.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Name und Vorname dürfen nicht leer sein", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				emailField.setText(generateEmail(name, vorname));
			}
		});
		panel.add(createEmailButton);

		// "Benutzer anzuzeigen" Button
		JButton showUsersButton = new JButton("Benutzer anzeigen");
		showUsersButton.setBounds(180, 140, 150, 30); // Position und Größe des Buttons.
		showUsersButton.addActionListener(e -> displayUsers(displayArea)); // Zeigt Benutzer an, wenn der Button
																			// geklickt wird
		panel.add(showUsersButton);

		// "Benutzer löschen" Button
		JButton deleteUserButton = new JButton("Benutzer löschen");
		deleteUserButton.setBounds(20, 180, 150, 30); // Position und Größe des Buttons
		deleteUserButton.addActionListener(e -> {
			// Aktion, wenn der Button geklickt wird:
			String input = JOptionPane.showInputDialog("Geben Sie die Benutzer-ID ein, die Sie löschen möchten:");
			if (input != null && !input.isEmpty()) {
				int userId = Integer.parseInt(input); // Liest die eingegebene ID
				userManager.deleteUser(userId); // Löscht den Benutzer mit dieser ID
			}
		});
		panel.add(deleteUserButton);

		// "Benutzer ändern" Button
		JButton updateUserButton = new JButton("Benutzer ändern");
		updateUserButton.setBounds(180, 180, 150, 30);
		updateUserButton.addActionListener(e -> {
			String input = JOptionPane.showInputDialog("Geben Sie die Benutzer-ID ein, die Sie ändern möchten:");
			if (input != null && !input.isEmpty()) {
				int userId = Integer.parseInt(input);
				new UpdateUserFrame(userId).setVisible(true);
			}
		});
		panel.add(updateUserButton);

		return panel;
	}

	private String generateEmail(String name, String vorname) {
		String email = vorname.substring(0, 1).toLowerCase() + name.toLowerCase() + "@email.com";

		return email;
	}

	// Panel für die Projektverwaltung (wie createUserPanel).
	private JPanel createProjectPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);

		JLabel titleLabel = new JLabel("Titel:");
		titleLabel.setBounds(20, 20, 100, 25);
		panel.add(titleLabel);

		JTextField titleField = new JTextField();
		titleField.setBounds(120, 20, 210, 25);
		panel.add(titleField);

		JLabel descriptionLabel = new JLabel("Beschreibung:");
		descriptionLabel.setBounds(20, 60, 100, 25);
		panel.add(descriptionLabel);

		JTextField descriptionField = new JTextField();
		descriptionField.setBounds(120, 60, 210, 25);
		panel.add(descriptionField);

		JLabel projectLeadLabel = new JLabel("Projektleiter:");
		projectLeadLabel.setBounds(20, 100, 100, 25);
		panel.add(projectLeadLabel);

		JTextField projectLeadField = new JTextField();
		projectLeadField.setBounds(120, 100, 210, 25);
		panel.add(projectLeadField);

		JTextArea displayArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setBounds(20, 220, 473, 200);
		panel.add(scrollPane);

		JButton createProjectButton = new JButton("Projekt erstellen");
		createProjectButton.setBounds(20, 140, 150, 30);
		createProjectButton.addActionListener(e -> {
			String title = titleField.getText();
			String description = descriptionField.getText();
			String projectLeadText = projectLeadField.getText();

			// Validierung: Prüfen, ob die Felder leer sind oder projectLeadId keine gültige
			// Zahl ist
			if (title.isEmpty() || description.isEmpty() || projectLeadText.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Alle Felder müssen ausgefüllt werden.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					int projectLeadId = Integer.parseInt(projectLeadText);
					projectManager.createProject(title, description, projectLeadId);

					titleField.setText("");
					descriptionField.setText("");
					projectLeadField.setText("");

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Projektleiter-ID muss eine gültige Zahl sein.", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.add(createProjectButton);

		JButton clearFieldsButton = new JButton("Felder löschen");
		clearFieldsButton.setBounds(340, 20, 150, 25);
		clearFieldsButton.addActionListener(e -> {
			titleField.setText("");
			descriptionField.setText("");
			projectLeadField.setText("");
			displayArea.setText("");
		});
		panel.add(clearFieldsButton);

		JButton showProjectsButton = new JButton("Projekte anzeigen");
		showProjectsButton.setBounds(180, 140, 150, 30);
		showProjectsButton.addActionListener(e -> displayProjects(displayArea));
		panel.add(showProjectsButton);

		JButton deleteProjectButton = new JButton("Projekt löschen");
		deleteProjectButton.setBounds(20, 180, 150, 30);
		deleteProjectButton.addActionListener(e -> {
			String input = JOptionPane.showInputDialog("Geben Sie die Projekt-ID ein, die Sie löschen möchten:");
			if (input != null && !input.isEmpty()) {
				int projectId = Integer.parseInt(input);
				projectManager.deleteProject(projectId);
			}
		});
		panel.add(deleteProjectButton);

		JButton updateProjectButton = new JButton("Projekt ändern");
		updateProjectButton.setBounds(180, 180, 150, 30);
		updateProjectButton.addActionListener(e -> {
			String input = JOptionPane.showInputDialog("Geben Sie die Projekt-ID ein, die Sie ändern möchten:");
			if (input != null && !input.isEmpty()) {
				int projectId = Integer.parseInt(input);
				String newTitle = JOptionPane.showInputDialog("Neuer Titel:");
				String newDescription = JOptionPane.showInputDialog("Neue Beschreibung:");
				int newProjectLeadId = Integer.parseInt(JOptionPane.showInputDialog("Neue Projektleiter-ID:"));
				projectManager.updateProject(projectId, newTitle, newDescription, newProjectLeadId);
			}
		});
		panel.add(updateProjectButton);

		return panel;
	}

	// Erstellt das Panel für die Aufgabenverwaltung (wie createUserPanel).
	private JPanel createTaskPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);

		JLabel titleLabel = new JLabel("Titel:");
		titleLabel.setBounds(20, 20, 100, 25);
		panel.add(titleLabel);

		JTextField titleField = new JTextField();
		titleField.setBounds(120, 20, 210, 25);
		panel.add(titleField);

		JLabel descriptionLabel = new JLabel("Beschreibung:");
		descriptionLabel.setBounds(20, 60, 100, 25);
		panel.add(descriptionLabel);

		JTextField descriptionField = new JTextField();
		descriptionField.setBounds(120, 60, 210, 25);
		panel.add(descriptionField);

		JLabel projectIdLabel = new JLabel("Projekt-ID:");
		projectIdLabel.setBounds(20, 100, 100, 25);
		panel.add(projectIdLabel);

		JTextField projectIdField = new JTextField();
		projectIdField.setBounds(120, 100, 210, 25);
		panel.add(projectIdField);

		JTextArea displayArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(displayArea);
		scrollPane.setBounds(20, 220, 473, 200);
		panel.add(scrollPane);

		JButton createTaskButton = new JButton("Aufgabe erstellen");
		createTaskButton.setBounds(20, 140, 150, 30);
		createTaskButton.addActionListener(e -> {
			String title = titleField.getText();
			String description = descriptionField.getText();
			String projectIdText = projectIdField.getText();

			// Validierung: Prüfen, ob die Felder leer sind oder projectId/userId keine
			// gültigen Zahlen sind
			if (title.isEmpty() || description.isEmpty() || projectIdText.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Alle Felder müssen ausgefüllt werden.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					int projectId = Integer.parseInt(projectIdText);
					taskManager.createTask(title, description, projectId);

					titleField.setText("");
					descriptionField.setText("");
					projectIdField.setText("");

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Projekt-ID und Benutzer-ID müssen gültige Zahlen sein.",
							"Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.add(createTaskButton);

		JButton clearFieldsButton = new JButton("Felder löschen");
		clearFieldsButton.setBounds(340, 20, 150, 25);
		clearFieldsButton.addActionListener(e -> {
			titleField.setText("");
			descriptionField.setText("");
			projectIdField.setText("");
			displayArea.setText("");
		});
		panel.add(clearFieldsButton);

		JButton showTasksButton = new JButton("Aufgaben anzeigen");
		showTasksButton.setBounds(180, 140, 150, 30);
		showTasksButton.addActionListener(e -> displayTasks(displayArea));
		panel.add(showTasksButton);

		JButton addUserToTaskButton = new JButton("Benutzer hinzufügen");
		addUserToTaskButton.setBounds(340, 140, 150, 30);
		addUserToTaskButton.addActionListener(e -> {
			String taskInput = JOptionPane.showInputDialog("Aufgaben-ID eingeben:");
			String userInput = JOptionPane.showInputDialog("Benutzer-ID eingeben:");
			if (taskInput != null && userInput != null && !taskInput.isEmpty() && !userInput.isEmpty()) {
				int taskId = Integer.parseInt(taskInput);
				int addUserId = Integer.parseInt(userInput);
				taskManager.addUserToTask(taskId, addUserId);
			}
		});
		panel.add(addUserToTaskButton);

		JButton deleteTaskButton = new JButton("Aufgabe löschen");
		deleteTaskButton.setBounds(20, 180, 150, 30);
		deleteTaskButton.addActionListener(e -> {
			String input = JOptionPane.showInputDialog("Geben Sie die Aufgaben-ID ein, die Sie löschen möchten:");
			if (input != null && !input.isEmpty()) {
				int taskId = Integer.parseInt(input);
				taskManager.deleteTask(taskId);
			}
		});
		panel.add(deleteTaskButton);

		// "Aufgabe ändern" Button hinzufügen
		JButton updateTaskButton = new JButton("Aufgabe ändern");
		updateTaskButton.setBounds(180, 180, 150, 30);
		updateTaskButton.addActionListener(e -> {
			String input = JOptionPane.showInputDialog("Geben Sie die Aufgaben-ID ein, die Sie ändern möchten:");
			if (input != null && !input.isEmpty()) {
				int taskId = Integer.parseInt(input);
				String newTitle = JOptionPane.showInputDialog("Neuer Titel:");
				String newDescription = JOptionPane.showInputDialog("Neue Beschreibung:");
				int newProjectId = Integer.parseInt(JOptionPane.showInputDialog("Neue Projekt-ID:"));
				taskManager.updateTask(taskId, newTitle, newDescription, newProjectId);
			}
		});
		panel.add(updateTaskButton);

		return panel;
	}

	// Methode zur Anzeige von Benutzern in der Textarea.
	private void displayUsers(JTextArea displayArea) {
		displayArea.setText(""); // Löscht den vorherigen Text.
		String users = userManager.showUsers(); // Holt die Benutzerdaten.
		displayArea.append(users); // Fügt die Daten zur Textarea hinzu.
	}

	// Methode zur Anzeige von Projekten in der Textarea.
	private void displayProjects(JTextArea displayArea) {
		displayArea.setText("");
		String projects = projectManager.showProjects();
		displayArea.append(projects);
	}

	private void displayProjectsAsTable(JTextArea displayArea) {
		TableModel model = projectManager.showProjectsAsTable(); // Verwende die Methode oben
		JTable projectTable = new JTable(model);

		JScrollPane scrollPane = new JScrollPane(projectTable);
		scrollPane.setBounds(20, 220, 473, 200); // Position und Größe anpassen
		displayArea.add(scrollPane); // Das Panel enthält die Tabelle
	}

	// Methode zur Anzeige von Aufgaben in der Textarea.
	private void displayTasks(JTextArea displayArea) {
		displayArea.setText("");
		String tasks = taskManager.showTasks();
		displayArea.append(tasks);
	}
}
