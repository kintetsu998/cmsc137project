package carwars.model;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import carwars.util.Config;

public class Bullet extends Entity implements Runnable {
	public static final int BULLET_WIDTH = 15;
	public static final int BULLET_HEIGHT = 10;
	public static final ArrayList<Bullet> bullets = new ArrayList<>();
	public static final Random rand = new Random();
	
	private Player player;
	private Image sprite;
	
	private float force;
	private int angle;
	private int front;
	private int damage;
	
	public Bullet(Image sprite, Player p) {
		super(
				p.getX() + Player.CAR_WIDTH/2 - BULLET_WIDTH/2, 
				p.getY() + Player.CAR_HEIGHT/2 - BULLET_HEIGHT/2
		);
		
		this.player = p;
		this.force = p.getForce();//(int) Math.floor((p.getForce()/Config.GAME_WIDTH)*100);
		this.angle = p.getAngle();
		this.front = p.getFront();
		this.damage = Bullet.rand.nextInt(8) + 8;
		this.sprite = sprite.getFlippedCopy(false, this.front == Player.LEFT);
		
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
				this.setX(this.getX() - xspeed);
			} else {
				
				this.setX(this.getX() + xspeed);
			}
			
			try{
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
			if(p == this.player) continue;
			else {
				//TODO: damage player
				intersects = this.hitBox().intersects(p.hitBox());
				if(intersects) {
					p.damage(this.damage);
					break;
				}
			}
		}
		
		if(intersects) return true;
		
		for(Terrain t : Terrain.terrains) {
			intersects = this.hitBox().intersects(t.hitBox());
			if(intersects) break;
		}
		
		return intersects;
	}
	
	public Rectangle hitBox() {
		return new Rectangle(this.getX(), this.getY(), BULLET_WIDTH, BULLET_HEIGHT);
	}
	
	@Override
	public String toString() {
		return this.player.getName() + " " + this.getX() + " " + this.getY() 
			+ " " + this.angle + " " + this.force + " " + this.damage;
	}
	
	public Image getSprite() {
		return this.sprite;
	}
	
	public int getFront() {
		return this.front;
	}
}
