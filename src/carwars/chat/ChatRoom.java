package carwars.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatRoom extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JLabel msgLabel;
    private JTextField tfServer, tfPort, tfMsg;
    private JTextArea ta;
    private int defaultPort;
    private String defaultHost;

    public ChatRoom(String host, int port, String username) {

        super("Car Wars - " + username);
        defaultPort = port;
        defaultHost = host;
        
        JPanel northPanel = new JPanel(new GridLayout(3,1));
        JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
        tfServer = new JTextField(host);
        tfPort = new JTextField("" + port);
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
        northPanel.add(tfMsg);
        add(northPanel, BorderLayout.NORTH);

        // The CenterPanel which is the chat room
        JPanel centerPanel = new JPanel(new GridLayout(1,1));
        add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400);
        setVisible(true);
    }

    // to be removed; added for test purposes
    public static void main(String[] args){
        ChatRoom cr = new ChatRoom("127.0.0.1", 8080, "Leensey");
    }

    // called by the Client to append text in the TextArea 
    void append(String str) {
        ta.append(str);
        ta.setCaretPosition(ta.getText().length() - 1);
    }

    public void actionPerformed(ActionEvent e) {

        // ok it is coming from the JTextField
        if(true) {
            // just have to send the message
            // client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, tf.getText()));             
            // tf.setText("");
            return;
        }
    }

}
