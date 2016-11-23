package carwars.model;

import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Rectangle;

import carwars.chat.TCPClient;
import carwars.chat.UDPClient;
import carwars.util.Code;
import carwars.util.Config;

public class Player extends Entity {
	public static final HashMap<String, Player> players = new HashMap<>();
	public static final int RIGHT = 0;
	public static final int LEFT = 1;
	
	static public final int CAR_WIDTH = 40;
	static public final int CAR_HEIGHT = 30;
	static public final int CAR_SPEED = 5;
	static public final int CAR_MAX_DIST = 150;
	
	static public final int MAX_HP = 100;
	
	static public final float JUMP_SPEED = -3.0f;
	
	
	private String name;
	private Animation spriteAnim;
	private Animation deadAnim;
	
	private int front;
	private int hp;
	private int movement;
	
	private int angle;
	private float force;
	private float vertSpeed;
	
	private boolean turn;
	private boolean goingUp;
	private boolean jumping;
	
	private TCPClient tcpClient;
	
	/** CONSTRUCTOR **/	
	public Player(String name, Animation spriteFile, Animation deadCar, float x) {
		super(x, 0);
		this.name = name;
		this.spriteAnim = spriteFile;
		this.deadAnim = deadCar;
		
		this.front = RIGHT;
		this.hp = MAX_HP;
		this.angle = 0;
		this.movement = CAR_MAX_DIST;
		this.force = 0;
		this.vertSpeed = 0;
		
		this.turn = false;
		this.goingUp = false;
		this.jumping = false;
		
		this.tcpClient = null;
		
		players.put(name, this);
	}
	
	public Player(String name, Animation spriteFile, Animation deadCar, float x, TCPClient c) {
		super(x, 0);
		this.name = name;
		this.spriteAnim = spriteFile;
		this.deadAnim = deadCar;
		
		this.front = RIGHT;
		this.hp = MAX_HP;
		this.angle = 0;
		this.movement = CAR_MAX_DIST;
		this.force = 0;
		this.vertSpeed = 0;
		
		this.turn = false;
		this.goingUp = false;
		this.jumping = false;
		
		this.tcpClient = c;
		
		players.put(name, this);
	}
	
	public void moveRight(){
		this.front = RIGHT;
		if(!this.intersectsTerrain(this.rightHitBox()) 
				&& this.getX() <= Config.GAME_WIDTH - CAR_WIDTH
				&& this.movement > 0  
				&& this.hp > 0 ) {
			this.setX(this.getX()+CAR_SPEED);
		}
	}
	
	public void moveLeft(){
		this.front = LEFT;
		if(!this.intersectsTerrain(this.leftHitBox()) 
				&& this.getX() >= 0
				&& this.movement > 0 
				&& this.hp > 0 ) {
			this.setX(this.getX()-CAR_SPEED);
		}
	}
	
	public void fall() {
		while(!this.isDead()) {
			boolean intersects = this.intersectsTerrain(this.hitBox());
			
			if(intersects && !goingUp){
				Player.this.jumping = false;
				
				while(this.intersectsTerrain(this.hitBox())) {
					this.setY(this.getY()-1);
				}
				this.setY(this.getY()+1);
				this.vertSpeed = 0;
			} else if(this.getY() >= Config.GAME_HEIGHT-CAR_HEIGHT) {
				this.damage(MAX_HP);
				this.end();
			} else {
				this.vertSpeed = (this.vertSpeed < Config.TERMINAL_SPEED)? 
						this.vertSpeed + Config.GRAVITY/100.0f: 
						Config.TERMINAL_SPEED;
				
				goingUp = !(vertSpeed >= 0);
				
				if(this.getY() <= Config.GAME_HEIGHT-CAR_HEIGHT)
					this.setY(this.getY() + this.vertSpeed);
			}

			try{
				Thread.sleep(5);
			} catch(Exception e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public void shoot(UDPClient udpClient) {
		//TODO: send bullet info via UDP
		Bullet b = new Bullet(this);
		udpClient.send(Code.CREATE_BULLET + b.toString());
		new Thread(b).start();
	}
	
	public Rectangle leftHitBox() {
		return new Rectangle(this.getX()-3, this.getY(), 3, CAR_HEIGHT-1);
	}
	
	public Rectangle rightHitBox() {
		return new Rectangle(this.getX()+CAR_WIDTH, this.getY(), 3, CAR_HEIGHT-1);
	}
	
	public boolean intersectsTerrain(Rectangle r) {
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
	
	public void jump() {
		if(!jumping) {
			this.vertSpeed = JUMP_SPEED;
			goingUp = true;
			jumping = true;
		}
	}
	
	public void start() {
		this.movement = CAR_MAX_DIST;
		this.force = 0;
		this.turn = true;
	}
	
	public void end() {
		this.turn = false;
	}
	
	public void update(float x, float y, int hp, int front, int angle) {
		this.setX(x);
		this.setY(y);
		this.hp = hp;
		this.front = front;
		this.angle = angle;
	}
	
	@Override
	public String toString() {
		return this.name + " " 
				+ this.getX() + " " 
				+ this.getY() + " " 
				+ this.hp + " " 
				+ this.front + " "
				+ this.angle;
	}
	
	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	public void setForce(float force) {
		this.force = force;
	}
	
	/** GETTERS **/
	public String getName() {
		return this.name;
	}
	
	public TCPClient getTCPClient() {
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
	
	public float getForce() {
		return this.force;
	}
	
	public boolean isDead() {
		return this.hp <= 0;
	}
	
	public boolean isTurn() {
		return this.turn;
	}
	
	public Animation getSpriteAnim() {
		return (this.isDead())? this.deadAnim: this.spriteAnim;
	}
	
	public Rectangle hitBox() {
		return new Rectangle(this.getX(), this.getY(), Player.CAR_WIDTH, Player.CAR_HEIGHT);
	}
}
