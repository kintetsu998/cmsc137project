package carwars.chat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import carwars.game.CarWars;
import carwars.model.Player;
import carwars.util.Code;
import carwars.util.Config;

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
		}
		
		return reply;
	}
	
	private void updatePlayer(String msg) {
		String[] tok = msg.split(" ");
		if(!player.getName().equals(tok[0])) {
			Player p = Player.players.get(tok[0]);
			
			p.update(Integer.parseInt(tok[1]),
					 Integer.parseInt(tok[2]),
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
		if(!player.isDead()) {
			this.send(Code.UPDATE_STATUS + player.toString());
		}
	}
	
	public void setPlayer(Player p) {
		this.player = p;
	}
}
