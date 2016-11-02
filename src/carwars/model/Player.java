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
	
	static public final int CAR_WIDTH = 40;
	static public final int CAR_HEIGHT = 30;
	static public final int CAR_SPEED = 3;
	
	static public final int MAX_HP = 100;
	
	static public final int MAX_ANGLE = 90;
	static public final int MIN_ANGLE = 0;
	
	private String name;
	private Socket tcpSocket;
	
	private int front;
	private int hp;
	private int angle;
	private boolean falling;
	
	/** CONSTRUCTOR **/
	public Player(String name, String sprite_file, int x, int y, String ip, int port) {
		super(sprite_file, x, y);
		this.name = name;
		this.connectTo(ip,  port);
		this.front = RIGHT;
		this.falling = false;
		this.hp = MAX_HP;
		this.angle = MIN_ANGLE;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite_file, int x, int y, String ip, int port) {
		super(sprite_file, x, y);
		this.name = name;
		this.connectTo(ip,  port);
		this.front = RIGHT;
		this.falling = false;
		this.hp = MAX_HP;
		this.angle = MIN_ANGLE;
		
		players.add(this);
	}
	
	public Player(String name, String sprite_file, int x, int y, Socket s) {
		super(sprite_file, x, y);
		this.name = name;
		this.tcpSocket = s;
		this.front = RIGHT;
		this.falling = false;
		this.hp = MAX_HP;
		this.angle = MIN_ANGLE;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite_file, int x, int y, Socket s) {
		super(sprite_file, x, y);
		this.name = name;
		this.tcpSocket = s;
		this.front = RIGHT;
		this.falling = false;
		this.hp = MAX_HP;
		this.angle = MIN_ANGLE;
		
		players.add(this);
	}
	
	public Player(String name, Image sprite, int x, int y) {
		this(name, sprite, x, y, null);
	}
	
	public Player(String name, Image sprite) {
		this(name, sprite, 0, 0, null);
	}
	
	public Player(String name, String sprite_file, int x, int y) {
		this(name, sprite_file, x, y, null);
	}
	
	public Player(String name, String sprite_file) {
		this(name, sprite_file, 0, 0, null);
	}
	
	public Player(String name, Image sprite, String ip, int port) {
		this(name, sprite, 0, 0, ip, port);
	}
	
	public Player(String name, String sprite_file, String ip, int port) {
		this(name, sprite_file, 0, 0, ip, port);
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
	
	public void moveRight(){
		this.front = RIGHT;
		if(!Player.intersectsTerrain(this.rightHitBox()) && this.getX() <= Config.GAME_WIDTH - CAR_WIDTH) {
			this.setX(this.getX()+CAR_SPEED);
		}
	}
	
	public void moveLeft(){
		this.front = LEFT;
		if(!Player.intersectsTerrain(this.leftHitBox()) && this.getX() >= 0){
			this.setX(this.getX()-CAR_SPEED);
		}
	}
	
	public void fall() {
		this.falling = true;
		
		while(true) {
			boolean intersects = Player.intersectsTerrain(this.hitBox());
			float vertSpeed = 0;
			
			if(intersects || this.getY() >= Config.GAME_HEIGHT-CAR_HEIGHT){
				this.falling = false;
				while(Player.intersectsTerrain(this.hitBox())) {
					this.setY(this.getY()-1f);
				}
				this.setY(this.getY()+1f);
				break;
			}
			else {
				this.falling = true;
				vertSpeed = (vertSpeed < Config.TERMINAL_SPEED)? vertSpeed + Config.GRAVITY: vertSpeed;
				if(this.getY() <= Config.GAME_HEIGHT-CAR_HEIGHT)
					this.setY(this.getY() + vertSpeed);
			}

			try{
				Thread.sleep(20);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Rectangle leftHitBox() {
		return new Rectangle(this.getX(), this.getY(), 1, CAR_HEIGHT-1);
	}
	
	private Rectangle rightHitBox() {
		return new Rectangle(this.getX()+CAR_WIDTH-1, this.getY(), 1, CAR_HEIGHT-1);
	}
	
	public static boolean intersectsTerrain(Rectangle r) {
		boolean intersects = false;
		for(Terrain e : Terrain.terrains) {
			intersects = r.intersects(e.hitBox());
			if(intersects) break;
		}
		
		return intersects;
	}
	
	public void damage(int dmg) {
		this.hp -= dmg;
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
	
	public int getHP() {
		return this.hp;
	}
	
	public int getAngle() {
		return this.angle;
	}
	
	public void setFalling(boolean flag) {
		this.falling = flag;
	}
	
	public boolean isFalling() {
		return falling;
	}
	
	public boolean isDead() {
		return this.hp <= 0;
	}
}
