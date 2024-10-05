package v2;

import java.awt.*;
import javax.swing.*;

public class DisplayProjectsFrame extends JFrame {

	private ProjectManager manager = new ProjectManager();

	public DisplayProjectsFrame() {
		setTitle("Projektübersicht");
		setSize(650, 400);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		ProjectTableModel model = new ProjectTableModel(manager.getProjects());
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		panel.add(scrollPane);
		add(panel);
	}
}
