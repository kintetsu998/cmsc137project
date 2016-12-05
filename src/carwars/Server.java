package carwars;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
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
	
	private Random rand;
	
	private boolean hasStarted;
	private boolean pause;
	private int wind;
	private int mapID;

	public Server(int port) throws IOException {
		serverSocket 	= new ServerSocket(port);
		udpSocket 		= new DatagramSocket(Config.UDP_SERVER_PORT);
		sockets 		= new HashMap<>();
		pList 			= new ArrayList<>();
		rand			= new Random();
		hasStarted 		= false;
		pause 			= false;
		if(Config.DEBUG) {
			mapID		= 0;
		} else {
			mapID		= rand.nextInt(5);
		}
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
							
							//sends a list of names for the newly joined player
							out.writeUTF(Code.PLAYER_LIST + getNames());
							
							//sends the new name in case there are duplicates
							out.writeUTF(Code.PLAYER_NAME + name);
							
							//sends to all that someone will join the game
							Server.this.sendToAll(Code.PLAYER_JOIN + name, server);
							
							while(true) {
								message = in.readUTF();
								if(message.equals(Code.START_CODE)) {
									hasStarted = (Config.DEBUG)? false: true;
									
									initializePList(getNames());
									if(Config.DEBUG) {
										Server.this.startUDP();
									} else {
										Server.this.startUDP();
									}
									Server.this.startGame();
									
									System.out.println("Game has started.");
								} else if(message.startsWith(Code.PAUSE_CODE)) {
									String pName = message.replace(Code.PAUSE_CODE, "");
									pause = !pause;
									
									if(pause) {
										Server.this.sendToAll(pName + " paused the game.", server);
									} else {
										Server.this.sendToAll(pName + " unpaused the game.", server);
									}
								} else {
									Server.this.sendToAll(name + ": " + message, server);
								}
							}
						} catch(SocketException e) {
							Server.this.sendToAll(name + " has disconnected.", server);
							sockets.remove(name);
							
							if(sockets.size() <= 0) {
								System.out.println("All players disconnected. Closing the server...");
								System.exit(0);
							}
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
					null, null,
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
						Server.this.udpSend(Code.MAP_ID + Integer.toString(Server.this.mapID));
						Server.this.udpSend(Code.GET_ALL_STATUS + returnStatuses());
						Server.this.udpSend(Code.WIND + Integer.toString(Server.this.wind));
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
		
		new Thread() {
			@Override
			public void run() {
				try{
					while(true) {
						wind = rand.nextInt(41)-20;
						Thread.sleep(30000); //30 secs
					}
				} catch(InterruptedException e) {
					return;
				}
			}
		}.start();
	}
	
	public void udpSend(String msg) throws Exception {
		byte[] buf = msg.getBytes();
		//InetAddress group = InetAddress.getByName(Config.UDP_SERVER_IP);
		SocketAddress address = new InetSocketAddress(Config.UDP_SERVER_IP, Config.UDP_CLIENT_PORT);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address);
		
		this.udpSocket.send(packet);
	}

	public static void main(String[] args) {
		int port;
		Server s = null;
		try{
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("Port number not defined.");
			port = Config.TCP_PORT;
		}
		
		System.out.println("Hosting in port " + port);

		try{
			s = new Server(port);
			s.start();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}