package carwars.init;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BGPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image img;

	  public BGPanel(String img) {
	    this(new ImageIcon(img).getImage());
	    super.setOpaque(false);
	  }

	  public BGPanel(Image img) {
		  this.img = img;
	  }

	  public void paintComponent(Graphics g) {
		  super.paintComponent(g);
		  g.drawImage(img, 0, 0, null);
	  }

}