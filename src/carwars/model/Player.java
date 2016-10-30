package carwars.model;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.newdawn.slick.Image;

public class Player extends Entity{
	public static final ArrayList<Player> players = new ArrayList<>();
	public static final byte RIGHT = 0;
	public static final byte LEFT = 1;
	
	private String name;
	private Socket tcpSocket;
	private byte front;
	
	/** CONSTRUCTOR **/
	public Player(String name, Image sprite, int x, int y) {
		super(sprite, x, y);
		this.name = name;
		this.tcpSocket = null;
		this.front = RIGHT;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite) {
		super(sprite, 0, 0);
		this.name = name;
		this.tcpSocket = null;
		this.front = RIGHT;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file, int x, int y) {
		super(sprite_file, x, y);
		this.name = name;
		this.tcpSocket = null;
		this.front = RIGHT;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file) {
		super(sprite_file, 0, 0);
		this.name = name;
		this.tcpSocket = null;
		this.front = RIGHT;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite, int x, int y, String ip, int port) {
		super(sprite, x, y);
		this.name = name;
		this.connectTo(ip,  port);
		this.front = RIGHT;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite, String ip, int port) {
		super(sprite, 0, 0);
		this.name = name;
		this.connectTo(ip,  port);
		this.front = RIGHT;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file, int x, int y, String ip, int port) {
		super(sprite_file, x, y);
		this.name = name;
		this.connectTo(ip,  port);
		this.front = RIGHT;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file, String ip, int port) {
		super(sprite_file, 0, 0);
		this.name = name;
		this.connectTo(ip,  port);
		this.front = RIGHT;
		
		players.add(this);
	}
	
	/** METHODS **/
	private void connectTo(String ip, int port) {
		try{
			tcpSocket = new Socket(ip, port);
		} catch(IOException e) {
			e.printStackTrace();
			System.err.println("Server not found.");
		}
	}
	
	public int moveRight(){
		return this.setX(this.getX()+1);
	}
	
	public int moveLeft(){
		return this.setX(this.getX()-1);
	}
	
	/** GETTERS **/
	public String getName() {
		return this.name;
	}
	
	public Socket getTcpSocket() {
		return this.tcpSocket;
	}
	
	public byte getFront() {
		return this.front;
	}
}
