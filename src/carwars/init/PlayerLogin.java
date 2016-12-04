package carwars.init;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import carwars.chat.TCPClient;
import carwars.util.Config;
import carwars.util.Resources;

public class PlayerLogin extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	// PlayerLogin attributes
    private int port;
    private String host;
    private JTextField tfHost, tfPort, tfUsername;
    private JButton logButton;
    private JButton howtoButton;
    private String username;
    private TCPClient client;
    private JLabel errLabel;
    private GameSetup gs;
    private boolean connected;
    private boolean isOpaque = false;
    
    public PlayerLogin(String h, int p) {

        super("Car Wars - Login");
        JLabel serverLabel = new JLabel("Server Address:  ");
        JLabel portLabel = new JLabel("Port Number:  ");
        JLabel usernameLabel = new JLabel("Username: ");
        Image img = new ImageIcon(Resources.PANEL_BG).getImage();
        BGPanel mainPanel = new BGPanel(img);
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        port = p;
        host = h;
        /* For inputs*/
        JPanel inputToConnect = new JPanel(new GridLayout(3,2,1,3));
        inputToConnect.setOpaque(isOpaque);

        tfHost = new JTextField(host);
        tfPort = new JTextField(Integer.toString(port));
        if(Config.DEBUG) {
	        tfUsername = new JTextField("Username");
        } else {
        	tfUsername = new JTextField();
        }

        serverLabel.setOpaque(isOpaque);
        portLabel.setOpaque(isOpaque);
        usernameLabel.setOpaque(isOpaque);
        
        inputToConnect.add(setLabelCustomLayout(serverLabel,FlowLayout.RIGHT,"Arial",Font.PLAIN,25));
        inputToConnect.add(setTextFieldCustomLayout(tfHost,"Arial",Font.PLAIN,25));
        inputToConnect.add(setLabelCustomLayout(portLabel,FlowLayout.RIGHT,"Arial",Font.PLAIN,25));
        inputToConnect.add(setTextFieldCustomLayout(tfPort,"Arial",Font.PLAIN,25));
        inputToConnect.add(setLabelCustomLayout(usernameLabel,FlowLayout.RIGHT,"Arial",Font.PLAIN,25));
        inputToConnect.add(setTextFieldCustomLayout(tfUsername,"Arial",Font.PLAIN,25));

        JPanel b = new JPanel(new GridLayout(1, 3));
        logButton = new JButton("Login");
        logButton.addActionListener(this);
        logButton.setOpaque(isOpaque);
        
        howtoButton = new JButton("How To");
        howtoButton.addActionListener(this);
        howtoButton.setOpaque(isOpaque);
        
        errLabel = new JLabel("");
        errLabel.setVerticalAlignment(JLabel.BOTTOM);
        errLabel.setOpaque(isOpaque);
        
        b.setOpaque(isOpaque);
        b.add(setButtonCustomLayout(howtoButton, FlowLayout.CENTER, "Arial",Font.PLAIN,25));
        b.add(setButtonCustomLayout(logButton, FlowLayout.CENTER, "Arial",Font.PLAIN,25));
        b.add(setLabelCustomLayout(errLabel, FlowLayout.CENTER, "Arial",Font.PLAIN,25));
        
        mainPanel.add(Box.createRigidArea(new Dimension(10, 320)));
        mainPanel.add(inputToConnect);
        mainPanel.add(b);
        mainPanel.add(Box.createVerticalGlue());
        
        add(mainPanel);
        this.setContentPane(mainPanel);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    private Component setLabelCustomLayout(JLabel component,int ALIGNMENT,String FONT_NAME,int FONT_STYLE,int FONT_SIZE) {
        FlowLayout layout = new FlowLayout();
        JPanel panel = new JPanel(layout);
        Font font = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);
        
        layout.setAlignment(ALIGNMENT);
        component.setFont(font);
        component.setForeground(Color.BLACK);
        panel.setOpaque(isOpaque);
        panel.add(component);
        
    	return panel;
    }
    
    private Component setTextFieldCustomLayout(Component component,String FONT_NAME,int FONT_STYLE,int FONT_SIZE) {
        FlowLayout layout = new FlowLayout();
        JPanel panel = new JPanel(layout);
        Font font = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);
        JTextField textField = (JTextField)component;
        
        layout.setAlignment(FlowLayout.LEFT);
        component.setFont(font);
        
        textField.setPreferredSize(new Dimension(300,30));
        panel.setOpaque(isOpaque);
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
        panel.setOpaque(isOpaque);
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
    
    public void login() {
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

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == logButton) {
        	login();
        } else {
        	showControls();
        }
    }
    
    public void showControls() {
    	new ControlFrame();
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
}