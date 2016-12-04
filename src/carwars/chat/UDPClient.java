package carwars.chat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;

import carwars.game.CarWars;
import carwars.model.Bullet;
import carwars.model.Player;
import carwars.util.Code;
import carwars.util.Config;
import carwars.util.Settings;

public class UDPClient extends Thread{
	private MulticastSocket udpSocket;
	private InetAddress ip;
	private Player player;
	private CarWars game;
	private boolean hasUpdated;
	
	public UDPClient(CarWars game) {
		try {
			this.game = game;
			this.udpSocket = new MulticastSocket(Config.UDP_CLIENT_PORT);
			this.ip = InetAddress.getByName(Config.UDP_SERVER_IP);
			
			if(Boolean.parseBoolean(Settings.getInstance().getProperty("os.isLinux"))) {
				boolean hasInterface = false;
				while(NetworkInterface.getNetworkInterfaces().hasMoreElements() && !hasInterface) {
					NetworkInterface ifc = NetworkInterface.getNetworkInterfaces().nextElement();
					
					while(ifc.getInetAddresses().hasMoreElements()) {
						InetAddress iface = ifc.getInetAddresses().nextElement(); 
						try{
							this.udpSocket.setInterface(iface);
							hasInterface = true;
							break;
						} catch(SocketException e) {
							continue;
						}
					}
				}
				
				if(!hasInterface) {
					throw new RuntimeException("No network interface found.");
				}
			}
			
			this.udpSocket.joinGroup(ip);
			this.hasUpdated = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			this.udpSocket.leaveGroup(ip);
			this.udpSocket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send(String message) {
		try{
			byte[] buf = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, Config.UDP_CLIENT_PORT);
			
			this.udpSocket.send(packet);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String receive() throws Exception {
		byte[] buf = new byte[Config.BUFFER_SIZE];
		DatagramPacket packet;
		String reply;
		
		packet = new DatagramPacket(buf, buf.length);
		this.udpSocket.receive(packet);
		
		reply = new String(packet.getData(), 0, packet.getLength());
		
		//Do something with the reply
		if(reply.startsWith(Code.GET_ALL_STATUS) && player != null && !hasUpdated) {
			game.updateStatuses(reply, player.getName());
			hasUpdated = true;
		} else if(reply.startsWith(Code.UPDATE_STATUS) && Player.players.size() > 0) {
			updatePlayer(reply.replace(Code.UPDATE_STATUS, ""));
		}else if(reply.startsWith(Code.CREATE_BULLET)) {
			createBullet(reply.replace(Code.CREATE_BULLET, ""));
		} else if(reply.startsWith(Code.MAP_ID)) {
			int mapID = Integer.parseInt(reply.replace(Code.MAP_ID, ""));
			game.setMapID(mapID);
		} else if(reply.startsWith(Code.WIND)) {
			game.setWind(Integer.parseInt(reply.replace(Code.WIND, "")));
		} else if(reply.startsWith(Code.PAUSE_CODE)) {
			game.togglePause();
		}
		
		return reply;
	}
	
	private void createBullet(String msg) {
		String[] tok = msg.split(" ");
		
		if(!tok[0].equals(player.getName())) {
			Player p = Player.players.get(tok[0]);
			Bullet b = new Bullet(p,
				Float.parseFloat(tok[1]),
				Float.parseFloat(tok[2]),
				Integer.parseInt(tok[3]),
				Float.parseFloat(tok[4]),
				Integer.parseInt(tok[5]),
				Integer.parseInt(tok[6])
			);
			
			new Thread(b).start();
		}
	}
	
	private void updatePlayer(String msg) {
		String[] tok = msg.split(" ");
		if(!player.getName().equals(tok[0])) {
			Player p = Player.players.get(tok[0]);
			
			p.update(Float.parseFloat(tok[1]),
					 Float.parseFloat(tok[2]),
					 Integer.parseInt(tok[3]),
					 Integer.parseInt(tok[4]),
					 Integer.parseInt(tok[5]));
		}
	}
	
	public void run() {
		while(true) {
			try {
				this.receive();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendStatus() {
		this.send(Code.UPDATE_STATUS + player.toString());
	}
	
	public void setPlayer(Player p) {
		this.player = p;
	}
}
