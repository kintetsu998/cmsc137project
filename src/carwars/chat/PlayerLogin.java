package carwars.chat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// import carwars.Main;

public class PlayerLogin extends JFrame implements ActionListener {

    // PlayerLogin attributes
    private int port;
    private String host;
    private JTextField tfHost, tfPort, tfUsername;
    private JLabel label;
    private JButton logButton;

    public PlayerLogin(String h, int p) {

        super("Car Wars - Login");
        port = p;
        host = h;

        JPanel mainPanel = new JPanel(new GridLayout(3,1));

        /* For logo */
        JPanel logo =  new JPanel(new GridLayout(1,2));
        ImageIcon image = new ImageIcon("resource/car.png");
        JLabel carLogo = new JLabel(image);
        logo.add(carLogo, SwingConstants.CENTER);
        logo.add(new JLabel("CAR WARS"), SwingConstants.CENTER);
        mainPanel.add(logo);

        /* For inputs*/
        JPanel inputToConnect = new JPanel(new GridLayout(3,2,1,3));

        tfHost = new JTextField(host);
        tfPort = new JTextField("" + port);
        tfHost.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(tfHost.getText().equals("Invalid host")){
                    tfHost.setText("");
                    tfHost.setForeground(Color.BLACK);
                }
            }
        });
        tfPort.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(tfPort.getText().equals("Invalid port")){
                    tfPort.setText("");
                    tfPort.setForeground(Color.BLACK);
                }
            }
        });

        inputToConnect.add(new JLabel("Server Address:  "));
        inputToConnect.add(tfHost);
        inputToConnect.add(new JLabel("Port Number:  "));
        inputToConnect.add(tfPort);

        label = new JLabel("Username: ");
        inputToConnect.add(label);
        tfUsername = new JTextField("Type username");
        tfUsername.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(tfUsername.getText().equals("Invalid username")){
                    tfUsername.setText("");
                    tfUsername.setForeground(Color.BLACK);
                }
            }
        });
        inputToConnect.add(tfUsername);

        mainPanel.add(inputToConnect, BorderLayout.CENTER);

        JPanel b = new JPanel();
        logButton = new JButton("Login");
        logButton.addActionListener(this);
        b.add(logButton);

        mainPanel.add(b);

        add(mainPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

    private boolean validInput(String txt){
        if(txt.equals("")||txt.equals(null)) return false;
        return true;
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        // if it is the Logout button
        if(o == logButton) {
            String t1 = tfUsername.getText();
            String t2 = tfHost.getText();
            String t3 = tfPort.getText();

            if(!validInput(t1)){
                tfUsername.setText("Invalid username");
                tfUsername.setForeground(Color.RED);
            }
            else if(!validInput(t2)){
                tfHost.setText("Invalid host");
                tfHost.setForeground(Color.RED);
            }
            else if(!validInput(t3)){
                tfPort.setText("Invalid port");
                tfPort.setForeground(Color.RED);
            }
            else{
                System.out.println("No null values");
            }
        }
    }

    public void mousePressed(MouseEvent e) {
       Object o = e.getSource();
    }

    public static void main(String[] args){
        new PlayerLogin("localhost", 8080);
    }
}