package carwars;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
 
public class GameSetup extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static JTextArea output;
    static JTable table;
    
    static int playersCount = 0;
    static DefaultTableModel tableModel;
    
    final static String WINDOW_TITLE_GAME_SETUP = "Car Wars - Game Setup";
    final static String TABLE_TITLE_PLAYERS = "Players";
    final static String TABLE_COLUMN_HEADER_PLAYER_NUMBER = "No.";
    final static String TABLE_COLUMN_HEADER_PLAYER_NAME = "Player Name";
    final static String TABLE_COLUMN_HEADER_STATUS = "Status";
    final static String STATUS_CONNECTED = "Connected";
    final static String STATUS_READY = "Ready";
    final static String NEWLINE = "\n";
    final static String BUTTON_READY = "READY";
    final static String BUTTON_START = "START";
    
    public static void addPlayer(String playerName) {
    	tableModel.addRow(new Object[]{String.valueOf(++playersCount), playerName, STATUS_CONNECTED});
    	output.append(playerName+" is connected."+NEWLINE);
    	output.setCaretPosition(output.getDocument().getLength());
    }
    
    public GameSetup() {
        super(new BorderLayout());
 
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
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame(WINDOW_TITLE_GAME_SETUP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        //Create and set up the content pane.
        GameSetup demo = new GameSetup();
        demo.setOpaque(true);
        frame.setContentPane(demo);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static class InputPlayer extends JFrame {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JButton addPlayerButton;
    	JTextField inputPlayerNameTextField;
    	public InputPlayer() {
    		setTitle("Add New Player");
    		setSize(300, 100);
    		setDefaultCloseOperation(EXIT_ON_CLOSE);
    		setLayout(new FlowLayout());
    		setLocationRelativeTo(null);
    		setAlwaysOnTop(true);
    		
    		addPlayerButton = new JButton("Add Player");
    		addPlayerButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					addPlayer(inputPlayerNameTextField.getText());
				}
			});
    		inputPlayerNameTextField = new JTextField(10);
    		
    		add(addPlayerButton);
    		add(inputPlayerNameTextField);
    		setVisible(true);
    	}
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                InputPlayer input = new InputPlayer();
                input.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

}