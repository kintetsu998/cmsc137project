package carwars.init;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import carwars.chat.ChatRoom;
import carwars.chat.Client;

public class PlayerLogin extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	// PlayerLogin attributes
    private int port;
    private String host;
    private JTextField tfHost, tfPort, tfUsername;
    private JLabel label;
    private JButton logButton;
    private String username;
    private Client client;
    private JLabel errLabel;
    private boolean connected;

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
                if(!connected){
                    errLabel.setText("");
                }
            }
        });
        tfPort.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e){
                if(!connected){
                    errLabel.setText("");
                }
            }
        });

        inputToConnect.add(new JLabel("Server Address:  "));
        inputToConnect.add(tfHost);
        inputToConnect.add(new JLabel("Port Number:  "));
        inputToConnect.add(tfPort);

        label = new JLabel("Username: ");
        inputToConnect.add(label);
        tfUsername = new JTextField("Username");
        tfUsername.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e){
                if(!connected){
                    errLabel.setText("");
                }
            }
        });
        inputToConnect.add(tfUsername);

        mainPanel.add(inputToConnect, BorderLayout.CENTER);

        JPanel b = new JPanel();
        logButton = new JButton("Login");
        logButton.addActionListener(this);
        b.add(logButton);
        errLabel = new JLabel("");
        b.add(errLabel);

        mainPanel.add(b);

        add(mainPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

    private boolean isEmptyInput(String txt){
        if(txt.equals("")||txt.equals(null)) return false;
        return true;
    }
    
    public static boolean validIP (String ip) {
    /* reference: http://stackoverflow.com/questions/4581877/validating-ipv4-string-in-java */
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if(o == logButton) {
            String t1 = tfUsername.getText().trim();
            String t2 = tfHost.getText().trim();
            String t3 = tfPort.getText().trim();

            if(!isEmptyInput(t1)){
                errLabel.setText("Invalid username");
                errLabel.setForeground(Color.RED);
                connected = false;
                return;
            }
            else if(!isEmptyInput(t2)){
                errLabel.setText("Invalid server address");
                errLabel.setForeground(Color.RED);
                connected = false;
                return;
            }
            else if(!isEmptyInput(t3)){
                errLabel.setText("Invalid port number");
                errLabel.setForeground(Color.RED);
                connected = false;
                return;
            } else if(t1.contains(" ")) {
            	errLabel.setText("Username should not contain spaces.");
                errLabel.setForeground(Color.RED);
                connected = false;
                return;
            }
            else{

            	// validate port number
            	try{
                	int p3 = Integer.parseInt(t3);
                }catch(NumberFormatException err){
                	errLabel.setText("Invalid port number");
                	errLabel.setForeground(Color.RED);
                	connected = false;
                	return;
                }

            	// validate IP address
            	if(!t2.equals("localhost")){
            		if(!validIP(t2)){
            			errLabel.setText("Invalid server address");
            			errLabel.setForeground(Color.RED);
            			connected = false;
            			return;
            		}
            	}
            	
            	this.username = t1;
            	client = new Client(username, host, port);
            	client.setChatRoom(new ChatRoom(client, port, host));
            	connected = client.connect();
            	
            	if(!connected){
                	errLabel.setText("Server - connection refused");
                	errLabel.setForeground(Color.RED);
                	connected = false;
                	return;
                }
            	else{
                	// launch app
                	this.dispose();
                	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            	        public void run() {
            	        	new GameSetup(PlayerLogin.this);
            	        }
            	    });
            	}

            }
        }
    }
    
    // getters
    public String getUsername(){
    	return username;
    }
    public String getHost(){
    	return host;
    }
    public int getPort(){
    	return port;
    }
    public Client getClient(){
    	return client;
    }
}