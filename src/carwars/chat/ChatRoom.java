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

    public ChatRoom(String host, int port, String username){//, Client client) {

        super("Car Wars - " + username);
        defaultPort = port;
        defaultHost = host;
        defaultClient = new Client(username, host, port, this);
        defaultClient.connect();
        
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
        tfMsg.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                defaultClient.sendMessage(tfMsg.getText().trim());
                tfMsg.setText("");
                // JOptionPane.showMessageDialog(null , "You've Submitted the name " + nameInput.getText());

            }
        });
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

    public static void main(String[] args){
        ChatRoom cr = new ChatRoom("127.0.0.1", 8080, "Leensey");
    }

    void append(String str) {
        ta.append(str);
        ta.setCaretPosition(ta.getText().length() - 1);
    }

    public void actionPerformed(ActionEvent ev) {
        if(ev.getSource() == tfMsg) {
            defaultClient.sendMessage(tfMsg.getText().trim());
            tfMsg.setText("");
            return;
        }
        
    }

}
