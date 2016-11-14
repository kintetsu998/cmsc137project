package carwars.init;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import carwars.game.CarWars;
 
public class GameSetup extends JPanel {
	public final static String WINDOW_TITLE_GAME_SETUP = "Car Wars - Game Setup";
	public final static String TABLE_TITLE_PLAYERS = "Players";
	public final static String TABLE_COLUMN_HEADER_PLAYER_NUMBER = "No.";
	public final static String TABLE_COLUMN_HEADER_PLAYER_NAME = "Player Name";
	public final static String TABLE_COLUMN_HEADER_STATUS = "Status";
	public final static String STATUS_CONNECTED = "Connected";
	public final static String STATUS_READY = "Ready";
	public final static String BUTTON_READY = "READY";
	public final static String BUTTON_START = "START";
    
	private static final long serialVersionUID = 1L;
	private JTextArea output;
    private JTable table;
    private JFrame frame;
    
    private static int playersCount = 0;
    private DefaultTableModel tableModel;
    
    public GameSetup(PlayerLogin pl) {
        super(new BorderLayout());
        
        //Create and set up the window.
        frame = new JFrame(WINDOW_TITLE_GAME_SETUP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        //Create and set up the content pane.
        addComponents(pl.getUsername());
        addPlayer(pl.getUsername());
        
        setOpaque(true);
        frame.setContentPane(this);
        
        //Display the window.
        frame.setSize(400, 600);
        frame.setResizable(false);
        frame.setVisible(true);
        
        pl.getClient().getChatRoom().setVisible(true);
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
        final JButton readyButton = new JButton(BUTTON_READY);
        final JButton startGameButton = new JButton(BUTTON_START);
        controlPane.add(readyButton);
        controlPane.add(startGameButton);
        
        startGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				try {
					AppGameContainer app = new AppGameContainer(new CarWars("Car Wars", username));
					app.setDisplayMode(800, 600, false); //create 800x600 frame
					app.setTargetFrameRate(60); //cap FPS to 60
					app.setShowFPS(false);
					app.setAlwaysRender(true);
					app.start();
					
				} catch (SlickException e) {
					e.printStackTrace();
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
        //XXX: next line needed if bottomHalf is a scroll pane:
        //bottomHalf.setMinimumSize(new Dimension(400, 50));
        bottomHalf.setPreferredSize(new Dimension(450, 110));
        splitPane.add(bottomHalf);
    }
    
    public void addPlayer(String playerName) {
    	System.out.println(playerName);
    	tableModel.addRow(new Object[]{new Integer(++GameSetup.playersCount).toString(), playerName, GameSetup.STATUS_CONNECTED});
    	output.append(playerName+" is connected.\n");
    	output.setCaretPosition(output.getDocument().getLength());
    }
    
    public JFrame getFrame() {
    	return this.frame;
    }
}