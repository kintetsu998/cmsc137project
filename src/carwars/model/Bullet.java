package carwars.model;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

import carwars.util.Config;
import carwars.util.Settings;

public class Bullet extends Entity implements Runnable {
	public static final int BULLET_WIDTH = Integer.parseInt(Settings.getInstance().getProperty("bullet.width"));
	public static final int BULLET_HEIGHT = Integer.parseInt(Settings.getInstance().getProperty("bullet.height"));
	public static final ArrayList<Bullet> bullets = new ArrayList<>();
	public static final Random rand = new Random();
	
	private Player player;
	
	
	private int angle;
	private int front;
	private int damage;
	private int rotate;
	private int wind;
	
	private float force;
	
	public Bullet(Player p) {
		
		super(
				p.getX() + Player.CAR_WIDTH/2 - BULLET_WIDTH/2, 
				p.getY() + Player.CAR_HEIGHT/2 - BULLET_HEIGHT/2
		);
		
		this.player = p;
		this.force = p.getForce();
		this.angle = p.getAngle();
		this.rotate = this.angle;
		this.wind = p.getWind();
		this.front = p.getFront();
		this.damage = Bullet.rand.nextInt(8) + 8;
		
		bullets.add(this);
	}
	
	public Bullet(Player p, 
			float x, float y, int angle, float force, int damage, int front) {
		
		super(x, y);
		
		this.player = p;
		this.force = force;
		this.angle = angle;
		this.rotate = this.angle;
		this.front = front;
		this.damage = damage;
		
		bullets.add(this);
	}
	
	@Override
	public void run() {
		float xspeed = (float) Math.floor((float) this.force * Math.cos(Math.toRadians(angle)));
		float yspeed =
				(this.front == Player.RIGHT)?
				(float) Math.floor((float) this.force * Math.sin(Math.toRadians(angle))):
				(float) Math.floor((float) this.force * Math.sin(Math.toRadians(-1*angle)));
		
		while((this.getX() < Config.GAME_WIDTH || this.getX() > 0)
				&& this.getY() < Config.GAME_HEIGHT
				&& !intersectsEntity()) {
			
			yspeed -= (float) Config.GRAVITY/100.0f;
			
			this.setY(this.getY() - yspeed);
			if(this.front == Player.LEFT) {
				xspeed -= (float) this.wind/500.0f;
				this.setX(this.getX() - xspeed);
			} else {
				xspeed += (float) this.wind/500.0f;
				this.setX(this.getX() + xspeed);
			}
			
			if(xspeed != 0) {
				rotate = (int) Math.floor(Math.toDegrees(Math.atan(yspeed/xspeed)));
			} else {
				rotate = (yspeed > 0)? 90: -90;
			}
			
			if(this.front == Player.RIGHT) {
				rotate *=-1;
			}
			
			try {
				Thread.sleep(5);
			} catch(Exception e) {
				Thread.currentThread().interrupt();
			}
		}
		
		bullets.remove(this);
	}
	
	private boolean intersectsEntity() {
		boolean intersects = false;
		
		for(Player p : Player.players.values()) {
			if(p == this.player || p.isDead()) continue;
			else {
				intersects = this.hitBox().intersects(p.hitBox());
				if(intersects) {
					p.damage(this.damage);
					break;
				}
			}
		}
		
		if(intersects) {
			return true;
		}
		
		for(Terrain t : Terrain.terrains) {
			intersects = this.hitBox().intersects(t.hitBox());
			if(intersects){
				break;
			}
		}
		
		return intersects;
	}
	
	public Rectangle hitBox() {
		return new Rectangle(this.getX(), this.getY(), BULLET_WIDTH, BULLET_HEIGHT);
	}
	
	@Override
	public String toString() {
		return this.player.getName() + " " + this.getX() + " " + this.getY() 
			+ " " + this.angle + " " + this.force + " " + this.damage + " " + this.front;
	}
	
	public void setWind(int wind) {
		this.wind = wind;
	}
	
	public int getFront() {
		return this.front;
	}
	
	public int getRotate() {
		return this.rotate;
	}
}
