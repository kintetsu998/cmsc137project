package carwars.init;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import carwars.chat.TCPClient;
import carwars.game.CarWars;
import carwars.util.Config;
 
public class GameSetup extends JPanel implements ActionListener {
	public final static String WINDOW_TITLE_GAME_SETUP = "Car Wars - Game Setup";
	public final static String TABLE_TITLE_PLAYERS = "Players";
	public final static String TABLE_COLUMN_HEADER_PLAYER_NUMBER = "No.";
	public final static String TABLE_COLUMN_HEADER_PLAYER_NAME = "Player Name";
	public final static String TABLE_COLUMN_HEADER_STATUS = "Status";
	public final static String STATUS_CONNECTED = "Connected";
	public final static String BUTTON_START = "START";
    
	private static final long serialVersionUID = 1L;
	private JTextArea output;
    private JTable table;
    private JFrame frame;
    private JLabel msgLabel;
    private JTextField tfMsg;
    
    private static int playersCount = 0;
    private DefaultTableModel tableModel;
    
    private TCPClient client;
    
    public GameSetup(PlayerLogin pl, TCPClient c) {
        super(new BorderLayout());
        
        this.client = c;
        
        //Create and set up the window.
        frame = new JFrame(WINDOW_TITLE_GAME_SETUP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        addComponents(pl.getUsername());
        
        setOpaque(true);
        frame.setContentPane(this);
        
        //Display the window.
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }
    
    public void showWindow() {
    	frame.setVisible(true);
    }
    
    private void addComponents(String username) {
    	String[] columnNames = { TABLE_COLUMN_HEADER_PLAYER_NUMBER, TABLE_COLUMN_HEADER_PLAYER_NAME, TABLE_COLUMN_HEADER_STATUS };
        String[][] tableData = {};
 
        table = new JTable();        
        tableModel = new DefaultTableModel(tableData, columnNames);
        table.setModel(tableModel);
        
        JScrollPane tablePane = new JScrollPane(table);
     
        //Build control area (use default FlowLayout).
        JPanel controlPane = new JPanel();
        final JButton startGameButton = new JButton(BUTTON_START);
        controlPane.add(startGameButton);
        
        startGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(tableModel.getRowCount() >= Config.MIN_PLAYERS) {
					GameSetup.this.client.startGame();
				} else {
					JOptionPane.showMessageDialog(frame, "Wait for more players...");
				}
			}
		});
 
        //Build output area.
        output = new JTextArea(1, 10);
        output.setEditable(false);
        JScrollPane outputPane = new JScrollPane(output,
                         ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                         ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
 
        //Do the layout.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        add(splitPane, BorderLayout.CENTER);
 
        JPanel topHalf = new JPanel();
        topHalf.setLayout(new BoxLayout(topHalf, BoxLayout.LINE_AXIS));
        JPanel listContainer = new JPanel(new GridLayout(1,1));
        JPanel tableContainer = new JPanel(new GridLayout(1,1));
        tableContainer.setBorder(BorderFactory.createTitledBorder(TABLE_TITLE_PLAYERS));
        tableContainer.add(tablePane);
        tablePane.setPreferredSize(new Dimension(420, 130));
        topHalf.setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
        topHalf.add(listContainer);
        topHalf.add(tableContainer);
 
        topHalf.setMinimumSize(new Dimension(250, 50));
        topHalf.setPreferredSize(new Dimension(200, 110));
        splitPane.add(topHalf);
 
        JPanel bottomHalf = new JPanel(new BorderLayout());
        bottomHalf.add(controlPane, BorderLayout.PAGE_START);
        bottomHalf.add(outputPane, BorderLayout.CENTER);

        msgLabel = new JLabel("Message", SwingConstants.CENTER);
        bottomHalf.add(msgLabel,BorderLayout.SOUTH);
        tfMsg = new JTextField("");
        tfMsg.setBackground(Color.WHITE);
        tfMsg.setForeground(Color.BLACK);
        tfMsg.addActionListener(this);
        bottomHalf.add(tfMsg, BorderLayout.SOUTH);

        //XXX: next line needed if bottomHalf is a scroll pane:
        bottomHalf.setPreferredSize(new Dimension(450, 110));
        splitPane.add(bottomHalf);
    }
    
    public void startGame() {
    	frame.dispose();
    	
    	new Thread() {
    		@Override
    		public void run() {
		    	try {
		    		//starts the game
					AppGameContainer app = new AppGameContainer(new CarWars("Car Wars", client));
					app.setDisplayMode(800, 600, false); //create 800x600 frame
					app.setTargetFrameRate(60); //cap FPS to 60
					app.setShowFPS(Config.DEBUG);
					app.setAlwaysRender(true);
					app.start();
				} catch (SlickException e) {
					JOptionPane.showMessageDialog(null, 
							e.getMessage(), 
							"Exception thrown", 
							JOptionPane.ERROR_MESSAGE);
					
					e.printStackTrace();
				}
    		}
    	}.start();
    }
    
    public void addPlayer(String playerName) {
    	tableModel.addRow(new Object[]{new Integer(++GameSetup.playersCount).toString(), playerName, GameSetup.STATUS_CONNECTED});
    	output.setCaretPosition(output.getDocument().getLength());
    }
    
    public JFrame getFrame() {
    	return this.frame;
    }
    
    public void displayInChat(String str) {
        output.append(str);
        tfMsg.setText("");
    }
    
    public void actionPerformed(ActionEvent ev) {
        if(ev.getSource() == tfMsg) {
        	try{
        		String message = tfMsg.getText().trim();
        		
            	if(message.length()>0) {
           		   client.sendMessage(message);
            	}
        	}catch(Exception ex){
        		JOptionPane.showMessageDialog(null , ex);
        		ex.printStackTrace();
        	}
        }
    }
}