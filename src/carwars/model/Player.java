package carwars.model;

import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Rectangle;

import carwars.chat.Client;
import carwars.util.Config;

public class Player extends Entity {
	public static final HashMap<String, Player> players = new HashMap<>();
	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	
	static public final int CAR_WIDTH = 40;
	static public final int CAR_HEIGHT = 30;
	static public final int CAR_SPEED = 2;
	
	static public final int MAX_HP = 100;
	
	static public final int MAX_ANGLE = 90;
	static public final int MIN_ANGLE = 0;
	
	static public final int MAX_FORCE = 100;
	
	static public final int CAR_MAX_DIST = 150;
	
	private String name;
	private Animation spriteAnim;
	
	private int front;
	private int hp;
	private int angle;
	private int movement;
	private int force;
	private int score;
	
	private boolean turn;
	
	private Client tcpClient;
	
	/** CONSTRUCTOR **/	
	public Player(String name, Animation sprite_file, int x) {
		super(x, 0);
		this.name = name;
		this.spriteAnim = sprite_file;
		
		this.front = RIGHT;
		this.hp = MAX_HP;
		this.angle = MIN_ANGLE;
		this.movement = CAR_MAX_DIST;
		this.force = 0;
		this.score = 0;
		
		this.turn = false;
		
		this.tcpClient = null;
		
		players.put(name, this);
	}
	
	public Player(String name, Animation sprite_file, int x, Client c) {
		super(x, 0);
		this.name = name;
		this.spriteAnim = sprite_file;
		
		this.front = RIGHT;
		this.hp = MAX_HP;
		this.angle = MIN_ANGLE;
		this.movement = CAR_MAX_DIST;
		this.force = 0;
		this.score = 0;
		
		this.turn = false;
		
		this.tcpClient = c;
		
		players.put(name, this);
	}
	
	public void moveRight(){
		this.front = RIGHT;
		if(!Player.intersectsTerrain(this.rightHitBox()) 
				&& this.getX() <= Config.GAME_WIDTH - CAR_WIDTH
				&& this.movement > 0
				&& this.turn) {
			this.setX(this.getX()+CAR_SPEED);
			this.movement -= CAR_SPEED;
		}
	}
	
	public void moveLeft(){
		this.front = LEFT;
		if(!Player.intersectsTerrain(this.leftHitBox()) 
				&& this.getX() >= 0
				&& this.movement > 0
				&& this.turn) {
			this.setX(this.getX()-CAR_SPEED);
			this.movement -= CAR_SPEED;
		}
	}
	
	public void fall() {
		while(!this.isDead()) {
			boolean intersects = Player.intersectsTerrain(this.hitBox());
			int vertSpeed = 0;
			
			if(intersects){
				while(Player.intersectsTerrain(this.hitBox())) {
					this.setY(this.getY()-1);
				}
				this.setY(this.getY()+1);
			} else if(this.getY() >= Config.GAME_HEIGHT-CAR_HEIGHT) {
				this.damage(MAX_HP);
				this.end();
			} else {
				vertSpeed = (vertSpeed < Config.TERMINAL_SPEED)? vertSpeed + Config.GRAVITY: Config.TERMINAL_SPEED;
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
	
	public void incAngle() {
		this.angle = (this.angle < Player.MAX_ANGLE)? this.angle + 1: this.angle;
	}
	
	public void decAngle() {
		this.angle = (this.angle > Player.MIN_ANGLE)? this.angle - 1: this.angle;
	}
	
	public void start() {
		this.movement = CAR_MAX_DIST;
		this.force = 0;
		this.turn = true;
	}
	
	public void end() {
		this.turn = false;
	}
	
	public void incForce() {
		this.force = (this.force < MAX_FORCE)? this.force + 1: this.force;
	}
	
	public void update(int x, int hp, int front) {
		this.setX(x);
		this.hp = hp;
		this.front = front;
	}
	
	@Override
	public String toString() {
		return this.name + " " + this.getX() + " " + this.hp + " " + this.front;
	}
	
	/** GETTERS **/
	public String getName() {
		return this.name;
	}
	
	public Client getTCPClient() {
		return this.tcpClient;
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
	
	public int getMovement() {
		return this.movement;
	}
	
	public int getForce() {
		return this.force;
	}
	
	public boolean isDead() {
		return this.hp <= 0;
	}
	
	public boolean isTurn() {
		return this.turn;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public Animation getSpriteAnim() {
		return this.spriteAnim;
	}
}
