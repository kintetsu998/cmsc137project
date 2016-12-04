package carwars.init;

import java.awt.Container;

import javax.swing.JFrame;

import carwars.util.Resources;

public class ControlFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public ControlFrame() {
		super();
		addComponents(getContentPane());
		setSize(1000, 425);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addComponents(Container pane) {
		pane.add(new BGPanel(Resources.CONTROL_BG));
	}
}
