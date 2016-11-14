package carwars.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import carwars.util.Config;

public class ChatRoom extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JLabel msgLabel;
    private JTextField tfServer, tfPort, tfMsg;
    private JTextArea ta;
    private int defaultPort;
    private String defaultHost;
    private Client defaultClient;

    public ChatRoom(Client client, int port, String host) {
        super("Chat room");
        defaultPort = port;
        defaultHost = host;
        defaultClient = client;
        
        JPanel northPanel = new JPanel(new GridLayout(3,1));
        JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
        tfServer = new JTextField(defaultHost);
        tfPort = new JTextField("" + defaultPort);
        tfServer.setEditable(false);
        tfPort.setEditable(false);
        tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

        serverAndPort.add(new JLabel("Server Address:  "));
        serverAndPort.add(tfServer);
        serverAndPort.add(new JLabel("Port Number:  "));
        serverAndPort.add(tfPort);
        serverAndPort.add(new JLabel(""));
        northPanel.add(serverAndPort);

        msgLabel = new JLabel("Message", SwingConstants.CENTER);
        northPanel.add(msgLabel);
        tfMsg = new JTextField("");
        tfMsg.setBackground(Color.WHITE);
        tfMsg.setForeground(Color.BLACK);
        tfMsg.addActionListener(this);
        northPanel.add(tfMsg);
        add(northPanel, BorderLayout.NORTH);

        ta = new JTextArea("", 80, 80);
        JPanel centerPanel = new JPanel(new GridLayout(1,1));
        centerPanel.add(new JScrollPane(ta));
        ta.setEditable(false);
        add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400);
    }

    public void append(String str) {
        ta.append(str);
        tfMsg.setText("");
    }

    public void actionPerformed(ActionEvent ev) {
        if(ev.getSource() == tfMsg) {
        	try{
        		String message = tfMsg.getText().trim();
            	System.out.println(message);
            	
            	//detects if the client tries to send a coded message
            	if(message.startsWith("join: ") || message.equals(Config.START_CODE)) {
            		this.append("Invalid message to send...");
            	}
            	//else it is a normal message
            	else if(message.length()>0) {
            		defaultClient.sendMessage(message);
            	}
        	}catch(Exception ex){
        		JOptionPane.showMessageDialog(null , ex);
        		ex.printStackTrace();
        	}
        }
    }

}
