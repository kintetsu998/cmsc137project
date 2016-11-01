package carwars.model;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import carwars.util.Config;

public class Player extends Entity{
	public static final ArrayList<Player> players = new ArrayList<>();
	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	
	private String name;
	private Socket tcpSocket;
	private int front;
	
	private boolean falling;
	
	/** CONSTRUCTOR **/
	public Player(String name, Image sprite, int x, int y) {
		super(sprite, x, y);
		this.name = name;
		this.tcpSocket = null;
		this.front = RIGHT;
		this.falling = false;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite) {
		super(sprite, 0, 0);
		this.name = name;
		this.tcpSocket = null;
		this.front = RIGHT;
		this.falling = false;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file, int x, int y) {
		super(sprite_file, x, y);
		this.name = name;
		this.tcpSocket = null;
		this.front = RIGHT;
		this.falling = false;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file) {
		super(sprite_file, 0, 0);
		this.name = name;
		this.tcpSocket = null;
		this.front = RIGHT;
		this.falling = false;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite, int x, int y, String ip, int port) {
		super(sprite, x, y);
		this.name = name;
		this.connectTo(ip,  port);
		this.front = RIGHT;
		this.falling = false;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite, String ip, int port) {
		super(sprite, 0, 0);
		this.name = name;
		this.connectTo(ip,  port);
		this.front = RIGHT;
		this.falling = false;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file, int x, int y, String ip, int port) {
		super(sprite_file, x, y);
		this.name = name;
		this.connectTo(ip,  port);
		this.front = RIGHT;
		this.falling = false;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file, String ip, int port) {
		super(sprite_file, 0, 0);
		this.name = name;
		this.connectTo(ip,  port);
		this.front = RIGHT;
		this.falling = false;
		
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
	
	public void moveRight(int delta){
		this.front = RIGHT;
		if(!Player.intersectsTerrain(this.rightHitBox())) {
			this.setX(this.getX()+1);
		}
		
		if(!Player.intersectsTerrain(this.hitBox())) {
			this.fall(delta);
		}
	}
	
	public void moveLeft(int delta){
		this.front = LEFT;
		if(!Player.intersectsTerrain(this.leftHitBox())){
			this.setX(this.getX()-1);
		}
		
		if(!Player.intersectsTerrain(this.hitBox())) {
			this.fall(delta);
		}
	}
	
	public void fall(int delta) {
		this.falling = true;
		
		while(true) {
			boolean intersects = Player.intersectsTerrain(this.hitBox());
			
			if(intersects || this.getY() >= Config.GAME_HEIGHT-Config.CAR_HEIGHT){
				this.falling = false;
				while(Player.intersectsTerrain(this.hitBox())) {
					this.setY(this.getY()-1);
				}
				this.setY(this.getY()+1);
				break;
			}
			else {
				this.falling = true;
				if(this.getY() <= Config.GAME_HEIGHT-Config.CAR_HEIGHT)
					this.setY(this.getY() + (Config.GRAVITY * delta));
			}
		}
	}
	
	public Rectangle hitBox() {
		return new Rectangle(this.getX(), this.getY(), Config.CAR_WIDTH, Config.CAR_HEIGHT);
	}
	
	public Rectangle leftHitBox() {
		return new Rectangle(this.getX(), this.getY(), 1, Config.CAR_HEIGHT-1);
	}
	
	public Rectangle rightHitBox() {
		return new Rectangle(this.getX()+Config.CAR_WIDTH-1, this.getY(), 1, Config.CAR_HEIGHT-1);
	}
	
	private static boolean intersectsTerrain(Rectangle r) {
		boolean intersects = false;
		for(Terrain e : Terrain.terrains) {
			intersects = r.intersects(e.hitBox());
			if(intersects) break;
		}
		
		return intersects;
	}
	
	/** GETTERS **/
	public String getName() {
		return this.name;
	}
	
	public Socket getTcpSocket() {
		return this.tcpSocket;
	}
	
	public int getFront() {
		return this.front;
	}
	
	public void setFalling(boolean flag) {
		this.falling = flag;
	}
	
	public boolean isFalling() {
		return falling;
	}
}
