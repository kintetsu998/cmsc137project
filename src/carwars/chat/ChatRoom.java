package carwars.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import carwars.init.PlayerLogin;

public class ChatRoom extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JLabel msgLabel;
    private JTextField tfServer, tfPort, tfMsg;
    private JTextArea ta;
    private int defaultPort;
    private String defaultHost;
    private Client defaultClient;

    public ChatRoom(PlayerLogin pl){//, Client client) {

        super("Car Wars - " + pl.getUsername());
        defaultPort = pl.getPort();
        defaultHost = pl.getHost();
        defaultClient = pl.getClient();
        
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
        setVisible(true);
    }

    void append(String str) {
        ta.append(str);
        tfMsg.setText("");
    }

    public void actionPerformed(ActionEvent ev) {
        if(ev.getSource() == tfMsg) {
        	try{
        		String message = new String(tfMsg.getText().trim());
            	System.out.println(message);
            	if(message.length()>0){
            		defaultClient.sendMessage(message);
            	}
        	}catch(Exception ex){
        		JOptionPane.showMessageDialog(null , ex);
        		ex.printStackTrace();
        	}
        }
    }

}
