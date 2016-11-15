package carwars.chat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import carwars.util.Config;

public class UDPClient {
	private MulticastSocket udpSocket;
	private InetAddress ip;
	
	public UDPClient() {
		try {
			this.udpSocket = new MulticastSocket(Config.UDP_CLIENT_PORT);
			this.ip = InetAddress.getByName(Config.UDP_SERVER_IP);
			this.udpSocket.joinGroup(ip);
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
		
		//TODO: do something with reply;
		System.out.println(reply);
		
		return reply;
	}
}
