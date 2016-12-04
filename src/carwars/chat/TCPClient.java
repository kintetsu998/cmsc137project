package carwars.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import carwars.game.CarWars;
import carwars.init.GameSetup;
import carwars.util.Code;

public class TCPClient {
    
    private DataInputStream in;
    private DataOutputStream out;
    private GameSetup gs;
    
    private int port;
    private String name;
    private String server;
    private Socket sock;
    private ArrayList<String> pNames;
    private CarWars game;
    
    private boolean hasName;
    private boolean hasList;

    public TCPClient(String name, String server, int port) {
        this.name = name;
        this.server = server;
        this.port = port;
        
        this.pNames = new ArrayList<>();
        this.game = null;
        
        this.hasName = false;
        this.hasList = false;
    }
    
    public void setGame(CarWars g) {
    	this.game = g;
    }

    public boolean connect() {
        // open socket connection
        try {
            sock = new Socket(server, port);
            
            // open input and output streams
            try{
                in  = new DataInputStream(sock.getInputStream());
                out = new DataOutputStream(sock.getOutputStream());
            }
            catch (IOException e1) {
                System.out.println("Exception creating new I/O streams: " + e1);
                return false;
            }

            out.writeUTF(this.name);

            // receive messages
            new Thread() {
                public void run() {
                    try{
                        String reply;
                        while(!sock.isClosed()){
                            reply = in.readUTF();
                            
                            //receives a code for a new client name
                           if(reply.startsWith(Code.PLAYER_NAME) && !hasName) {
                            	String[] tok = reply.split(" ");
                            	
                            	TCPClient.this.name = tok[1];
                            	hasName = true;
                            	
                            	showMessage("You are now connected as " + tok[1] + ".");
                            } 
                            //receives a code for someone joining the game
                            else if(reply.startsWith(Code.PLAYER_JOIN)) {
                            	String[] tok = reply.split(" ");
                            	
                            	TCPClient.this.pNames.add(tok[1]);
                            	gs.addPlayer(tok[1]);
                            	
                            	showMessage(tok[1] + " has connected to the game!");
                            }
                            //receives a code for a list of current players in the room
                            else if(reply.startsWith(Code.PLAYER_LIST) && !hasList) {
                            	addPNames(reply.split(" "));
                            	hasList = true;
                            }
                            //receives a code that should start the game;
                            else if(reply.equals(Code.START_CODE)) {
                            	gs.startGame();
                            }
                            //else, no code detected. display the message on screen
                            else {
                            	showMessage(reply);
                            }
                        }
                    } catch(Exception e1) {
                        JOptionPane.showMessageDialog(null, "The server closed.");
                        System.exit(1);
                    }
                }
                
                private void addPNames(String[] tok) {
                	for(int i = 1; i < tok.length; i++) {
                		if(!tok[i].trim().equals("")) {
                			TCPClient.this.pNames.add(tok[i]);
                			gs.addPlayer(tok[i]);
                		}
                	}
                }
            }.start();
        } catch (ConnectException e2) {
            e2.printStackTrace();
            return false;
        } catch (IOException e3) {
            e3.printStackTrace();
            return false;
        }
        return true;
    }
    
    private void showMessage(String message) {
    	if(game != null) {
    		game.updateChat(message);
    	} else {
    		display(message);
    	}
    }

    public void sendMessage(String message) {
        try {        	
        	if(Code.codeExists(message)) {
        		message = "Invalid message to send...";
        	} else {
        		out.writeUTF(message);
        	}
        	
            if(game != null) {
        		game.updateChat(name + ": " + message);
        	} else {
        		display(name + ": " + message);
        	}
        }
        catch(IOException e) {
        	e.printStackTrace();
        }
    }
    
    public void startGame() {
    	try {
            out.writeUTF(Code.START_CODE);
        }
        catch(IOException e) {
        	e.printStackTrace();
        }
    }
    
    public void pauseGame(String pName) {
    	try {
            out.writeUTF(Code.PAUSE_CODE + pName);
        }
        catch(IOException e) {
        	e.printStackTrace();
        }
    }

    public String getName(){
        return this.name;
    }
    public void setGameSetup(GameSetup gs) {
    	this.gs = gs;
    	
    	for(String player : pNames) {
    		gs.addPlayer(player);
    	}
    }
    
    public void setName(String n){
        name = n;
    }
    
    public Socket getSocket(){
    	return this.sock;
    }

    public void display(String msg) {
        gs.displayInChat(msg + "\n");
    }
}