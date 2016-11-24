package carwars.init;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Mechanics extends JFrame{

	private static final long serialVersionUID = 1L;
    private JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
    private JLabel label = new JLabel("Car Wars - Mechanics", JLabel.CENTER);
    private Timer timer = new Timer(100, new ActionListener() {

        private int counter = 1;

        @Override
        public void actionPerformed(ActionEvent ae) {
            label.setText(String.valueOf(counter));
            bar.setValue(++counter);
            if (counter > 100) {
                timer.stop();
            }
        }
    });

    Mechanics() {

    	super("Car Wars - Mechanics");
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridLayout(0, 1));
        bar.setValue(0);
        timer.start();
        panel.add(bar);
        panel.add(label);
        
        //Display the window.
        this.add(panel);
        this.setSize(400, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Mechanics cdpb = new Mechanics();
            }
        });
    }
}