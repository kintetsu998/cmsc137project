package carwars.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import carwars.model.Player;
import carwars.util.Code;
import carwars.util.Config;

public class Server extends Thread {
	private ServerSocket serverSocket;
	private DatagramSocket udpSocket;
	private Thread udpSend;
	
	private HashMap<String, Socket> sockets;
	private ArrayList<Player> pList;
	
	private int stop;
	private boolean hasStarted;

	public Server(int port) throws IOException {
		serverSocket 	= new ServerSocket(port);
		udpSocket 		= new DatagramSocket(Config.UDP_SERVER_PORT);
		sockets 		= new HashMap<>();
		pList 			= new ArrayList<>();
		hasStarted 		= false;
		stop 			= 0;
	}

	public void run(){
		System.out.println("Server waiting... ");

		try{
			while(true){
				Socket server = serverSocket.accept();
				
				if(hasStarted) {
					DataOutputStream out = new DataOutputStream(server.getOutputStream());
					out.writeUTF("The game has already started.");
				} else if(sockets.size() > Config.MAX_PLAYERS) {
					DataOutputStream out = new DataOutputStream(server.getOutputStream());
					out.writeUTF("The server's max players reached. Cannot accept anymore players.");
				}
				

				new Thread(){
					@Override
					public void run(){
						DataInputStream in;
						DataOutputStream out;
						String message;
						String name = null;
						
						try {
							in = new DataInputStream(server.getInputStream());
							name = Server.this.checkDuplicate(in.readUTF());
							
							sockets.put(name, server);

							out = new DataOutputStream(server.getOutputStream());
							//out.writeUTF("You are now connected as " + name);
							
							//sends a list of names for the newly joined player
							out.writeUTF(Code.PLAYER_LIST + getNames());
							
							//sends the new name in case there are duplicates
							out.writeUTF(Code.PLAYER_NAME + name);
							
							//sends to all that someone will join the game
							Server.this.sendToAll(Code.PLAYER_JOIN + name, server);
							
							//send to all that someone connected the game
							//Server.this.sendToAll(name + " connected!", server);
							
							while(true) {
								message = in.readUTF();
								if(message.equals(Code.START_CODE)) {
									hasStarted = (Config.DEBUG)? false: true;
									
									initializePList(getNames());
									Server.this.startUDP();
									Server.this.startGame();
									
									System.out.println("Game has started.");
								} else if(message.equals(Code.UDP_STOP_STATUS)) {
									stop++;
									if(stop == sockets.size() && !udpSend.isInterrupted()) {
										udpSend.interrupt();
									}
								} else {
									Server.this.sendToAll(name + ": " + message, server);
								}
							}
						} catch(SocketException e) {
							Server.this.sendToAll(name + " has disconnected.", server);
							sockets.remove(name);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initializePList(String names) {
		String[] tok = names.split(" ");
		Random r = new Random();
		
		for(String s : tok) {
			pList.add(new Player(s, 
					null, 
					r.nextInt(Config.GAME_WIDTH - Player.CAR_WIDTH), 
					null)
			);
		}
	}

	private String getNames() {
		String str = "";
		
		for(String s : sockets.keySet()) {
			str += s + " ";
		}
		
		return str;
	}

	private String checkDuplicate(String name) {
		for(String s : sockets.keySet()) {
			if(s.equals(name)) {
				return checkDuplicate2(name, name, 0);
			}
		}

		return name;
	}

	private String checkDuplicate2(String name, String base, int i) {
		for(String s : sockets.keySet()) {
			if(s.equals(name)) {
				return checkDuplicate2(base + (i+1), base, i+1);
			}
		}

		return name;
	}

	public void sendToAll(String message, Socket socket) {
		for(String name : sockets.keySet()) {
			Socket s = sockets.get(name);

			if(socket != s && !s.isClosed()) {
				try{
					DataOutputStream out = new DataOutputStream(s.getOutputStream());
					out.writeUTF(message);
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void startGame() {
		for(String name : sockets.keySet()) {
			Socket s = sockets.get(name);

			try{
				DataOutputStream out = new DataOutputStream(s.getOutputStream());
				out.writeUTF(Code.START_CODE);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startUDP() {
		udpSend = new Thread() {
			@Override
			public void run() {
				while(!this.isInterrupted()) {
					try{
						Server.this.udpSend(Code.GET_ALL_STATUS + returnStatuses());
						Thread.sleep(100);
					} catch(Exception e) {
						Thread.currentThread().interrupt();
					}
				}
			}
			
			private String returnStatuses() {
				String status = " ";
				
				for(Player p : pList) {
					status += p.toString() + ",";
				}
				
				return status;
			}
		};
		udpSend.start();
	}
	
	public void udpSend(String msg) throws Exception {
		byte[] buf = msg.getBytes();
		InetAddress group = InetAddress.getByName(Config.UDP_SERVER_IP);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, group, Config.UDP_CLIENT_PORT);
		
		this.udpSocket.send(packet);
	}

	public static void main(String[] args) {
		int port;
		Server s = null;
		try{
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = 8080;
			return;
		}

		try{
			s = new Server(port);
			s.start();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}