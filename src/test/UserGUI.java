package prototype;

import java.awt.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class UserGUI extends JFrame {

	public UserGUI() {
		setTitle("Benutzerübersicht");
		setSize(530, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel userInterface = userPanel();
		tabbedPane.addTab("Übersicht", userInterface);
		
		JPanel taskInteface = taskPanel();
		tabbedPane.addTab("Aufgaben", taskInteface);

	}

	private JPanel userPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(getLayout());
		
		
		return null;
	}

	private JPanel taskPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(getLayout());
		return null;
	}

}
