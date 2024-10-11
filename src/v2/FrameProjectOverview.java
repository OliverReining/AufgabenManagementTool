package v2;

import java.awt.*;
import javax.swing.*;

public class FrameProjectOverview extends JFrame {

	private ProjectManager pMan;
	private Project project;
	private LogManager log;

	public FrameProjectOverview(Project project, LogManager log, ProjectManager pMan) {
		this.pMan = pMan;
		this.project = project;
		this.log = log;
		setTitle("Projektübersicht");
		setSize(650, 400);
		setLocationRelativeTo(null);
		
	}
}
