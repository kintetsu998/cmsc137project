package carwars.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

import carwars.init.GameSetup;
import carwars.util.Code;

public class Client {
    
    private DataInputStream in;
    private DataOutputStream out;
    private ChatRoom gui;
    private GameSetup gs;
    
    private int port;
    private String name;
    private String server;
    private Socket sock;
    private ArrayList<String> pNames;
    
    private boolean hasName;
    private boolean hasList;

    public Client(String name, String server, int port) {
        this.name = name;
        this.server = server;
        this.port = port;
        
        this.pNames = new ArrayList<>();
        
        this.hasName = false;
        this.hasList = false;
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
                            while((reply = in.readUTF()) == null){}
                            
                            //receives a code for a new client name
                            if(reply.startsWith("name: ") && !hasName) {
                            	String[] tok = reply.split(" ");
                            	
                            	Client.this.name = tok[1];
                            	hasName = true;
                            } 
                            //receives a code for someone joining the game
                            else if(reply.startsWith("join: ")) {
                            	String[] tok = reply.split(" ");
                            	
                            	Client.this.pNames.add(tok[1]);
                            	gs.addPlayer(tok[1]);
                            }
                            //receives a code for a list of current players in the room
                            else if(reply.startsWith("list: ") && !hasList) {
                            	addPNames(reply.split(" "));
                            	hasList = true;
                            }
                            //receives a code that should start the game;
                            else if(reply.equals(Code.START_CODE)) {
                            	gs.startGame(Client.this);
                            }
                            //else, no code detected. display the message on screen
                            else {
                            	display(reply);
                            }
                        }
                    } catch(Exception e1) {
                        display("Exception in thread: " + e1);
                    }
                }
                
                private void addPNames(String[] tok) {
                	for(int i = 1; i < tok.length; i++) {
                		if(!tok[i].trim().equals("")) {
                			Client.this.pNames.add(tok[i]);
                		}
                	}
                }
            }.start();

        } catch (ConnectException e2) {
            display("Exception connecting to server: " + e2);
            System.out.println(e2);
            return false;
        } catch (IOException e3) {
            // e.printStackTrace();
            display("Exception in server/port I/O: " + e3);
            return false;
        }
        return true;
    }

    public void sendMessage(String message){
        try {
            out.writeUTF(message);
            display(name + ": " + message);
        }
        catch(IOException e) {
            display("Exception sending to server: " + e);
        }
    }
    
    public void startGame() {
    	try {
            out.writeUTF(Code.START_CODE);
        }
        catch(IOException e) {
            display("Exception sending to server: " + e);
        }
    }

    public String getName(){
        return this.name;
    }
    
    public void setChatRoom(ChatRoom gui) {
    	this.gui = gui;
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

    // public static void main(String[] args) {
    //     Scanner sc = new Scanner(System.in);
    //     String serverName;
    //     String name;
    //     int port;

    //     if(args.length < 2) {
    //         help();
    //         return;
    //     }

    //     serverName = args[0];
    //     try{
    //         port = Integer.parseInt(args[1]);
    //     } catch (NumberFormatException e) {
    //         help();
    //         return;
    //     }

    //     System.out.print("Enter your name: ");
    //     name = sc.nextLine();
        
    //     new Client(name).connect(serverName, port); 
    // }

    public void display(String msg) {
        gui.append(msg + "\n");      // append to the PlayerLogin JTextArea (or whatever)
    }
    
    public ChatRoom getChatRoom() {
    	return this.gui;
    }

    /*private static void help() {
        System.out.println("Using the Client class:");
        System.out.println("java Client [ip address] [port number]");
    }*/
}