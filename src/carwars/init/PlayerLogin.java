package carwars.init;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import carwars.chat.TCPClient;
import carwars.util.Config;

public class PlayerLogin extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	// PlayerLogin attributes
    private int port;
    private String host;
    private JTextField tfHost, tfPort, tfUsername;
    private JButton logButton;
    private String username;
    private TCPClient client;
    private JLabel errLabel;
    private GameSetup gs;
    private boolean connected;
    
    public PlayerLogin(String h, int p) {

        super("Car Wars - Login");
        port = p;
        host = h;
        
        JPanel mainPanel = new JPanel(new GridLayout(3,1));
        
        /* For logo */
        GridLayout layout = new GridLayout(1,2);
        JPanel logo =  new JPanel(layout);
        ImageIcon image = new ImageIcon("resource/car.png");
        JLabel carLogo = new JLabel(image);
        logo.add(carLogo, SwingConstants.CENTER);
        JLabel carWarsLabel = new JLabel("CAR WARS");
        logo.add(setLabelCustomLayout(carWarsLabel,FlowLayout.LEFT,"Arial",Font.ITALIC,55), SwingConstants.CENTER);
        mainPanel.add(logo);

        /* For inputs*/
        JPanel inputToConnect = new JPanel(new GridLayout(3,2,1,3));

        if(Config.DEBUG) {
	        tfHost = new JTextField(host);
	        tfPort = new JTextField(Integer.toString(port));
	        tfUsername = new JTextField("Username");
        } else {
        	tfHost = new JTextField();
        	tfPort = new JTextField();
        	tfUsername = new JTextField();
        }

        inputToConnect.add(setLabelCustomLayout(new JLabel("Server Address:  "),FlowLayout.LEFT,"Arial",Font.PLAIN,25));
        inputToConnect.add(setTextFieldCustomLayout(tfHost,"Arial",Font.PLAIN,25));
        inputToConnect.add(setLabelCustomLayout(new JLabel("Port Number:  "),FlowLayout.LEFT,"Arial",Font.PLAIN,25));
        inputToConnect.add(setTextFieldCustomLayout(tfPort,"Arial",Font.PLAIN,25));
        inputToConnect.add(setLabelCustomLayout(new JLabel("Username: "),FlowLayout.LEFT,"Arial",Font.PLAIN,25));
        
        
        inputToConnect.add(setTextFieldCustomLayout(tfUsername,"Arial",Font.PLAIN,25));

        mainPanel.add(inputToConnect, BorderLayout.CENTER);

        JPanel b = new JPanel(new GridLayout(2, 1));
        logButton = new JButton("Login");
        logButton.addActionListener(this);
        errLabel = new JLabel("");
        errLabel.setVerticalAlignment(JLabel.BOTTOM);
        b.add(setLabelCustomLayout(errLabel,FlowLayout.CENTER, "Arial",Font.PLAIN,25));
        b.add(setButtonCustomLayout(logButton,FlowLayout.CENTER, "Arial",Font.PLAIN,25));

        mainPanel.add(b);

        add(mainPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private Component setLabelCustomLayout(Component component,int ALIGNMENT,String FONT_NAME,int FONT_STYLE,int FONT_SIZE) {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(ALIGNMENT);
        layout.setHgap(20);
        JPanel panel = new JPanel(layout);
        Font font = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);
        component.setFont(font);
        panel.add(component);
    	return panel;
    }
    
    private Component setTextFieldCustomLayout(Component component,String FONT_NAME,int FONT_STYLE,int FONT_SIZE) {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        layout.setHgap(20);
        JPanel panel = new JPanel(layout);
        Font font = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);
        component.setFont(font);
        
        JTextField textField = (JTextField)component;
        textField.setPreferredSize(new Dimension(350,50));
        panel.add(component);
    	return panel;
    }
    
    private Component setButtonCustomLayout(Component component,int ALIGNMENT,String FONT_NAME,int FONT_STYLE,int FONT_SIZE) {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(ALIGNMENT);
        layout.setHgap(20);
        JPanel panel = new JPanel(layout);
        Font font = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);
        JButton button = (JButton)component;
        button.setFont(font);
        button.setPreferredSize(new Dimension(200,50));
        panel.add(component);
    	return panel;
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
            } else if(t1.contains("@")) {
            	errLabel.setText("Username should not contain @ symbol.");
                errLabel.setForeground(Color.RED);
                connected = false;
                return;
            }
            else{

            	// validate port number
            	try{
                	this.port = Integer.parseInt(t3);
                }catch(NumberFormatException err){
                	errLabel.setText("Invalid port number");
                	errLabel.setForeground(Color.RED);
                	connected = false;
                	return;
                }

            	this.host = t2;
            	this.username = t1;
            	this.client = new TCPClient(username, host, port);
            	this.gs = new GameSetup(this, client);
            	//client.setChatRoom(new ChatRooms(client, port, host));
            	client.setGameSetup(gs);
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
                	gs.showWindow();
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
    public TCPClient getClient(){
    	return client;
    }
    
    public static void main(String []args) {
    	PlayerLogin newPlayLogin = new PlayerLogin("127.0.0.1", 3);
    	newPlayLogin.setVisible(true);
    }
}