package carwars.model;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.newdawn.slick.Image;

public class Player extends Entity{
	public static final ArrayList<Player> players = new ArrayList<>();
	
	private String name;
	private Socket socket;
	
	/** CONSTRUCTOR **/
	public Player(String name, Image sprite, int x, int y) {
		super(sprite, x, y);
		this.name = name;
		this.socket = null;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite) {
		super(sprite, 0, 0);
		this.name = name;
		this.socket = null;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file, int x, int y) {
		super(sprite_file, x, y);
		this.name = name;
		this.socket = null;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file) {
		super(sprite_file, 0, 0);
		this.name = name;
		this.socket = null;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite, int x, int y, String ip, int port) {
		super(sprite, x, y);
		this.name = name;
		this.connectTo(ip,  port);
		
		players.add(this);
	}
	
	public Player(String name, Image sprite, String ip, int port) {
		super(sprite, 0, 0);
		this.name = name;
		this.connectTo(ip,  port);
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file, int x, int y, String ip, int port) {
		super(sprite_file, x, y);
		this.name = name;
		this.connectTo(ip,  port);
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file, String ip, int port) {
		super(sprite_file, 0, 0);
		this.name = name;
		this.connectTo(ip,  port);
		
		players.add(this);
	}
	
	/** METHODS **/
	private void connectTo(String ip, int port) {
		try{
			socket = new Socket(ip, port);
		} catch(IOException e) {
			e.printStackTrace();
			System.err.println("Server not found.");
		}
	}
	
	/** GETTERS **/
	public String getName() {
		return this.name;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
}
