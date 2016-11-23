package carwars.model;

import org.newdawn.slick.geom.Rectangle;

import carwars.util.Config;

public class Bullet extends Entity implements Runnable {
	public static final int BULLET_WIDTH = 15;
	public static final int BULLET_HEIGHT = 10;
	
	private Player player;
	private float force;
	private int angle;
	private int front;
	
	public Bullet(Player p, int angle, float force, int front) {
		super(p.getX(), p.getY());
		this.player = p;
		this.force = force;
		this.angle = angle;
		this.front = front;
	}
	
	@Override
	public void run() {
		while(this.getY() < Config.GAME_WIDTH || !intersectsEntity()) {
			// TODO Launch Bullet
		}
	}
	
	private boolean intersectsEntity() {
		boolean intersects = false;
		for(Entity e : Entity.entities) {
			intersects = this.hitBox().intersects(e.hitBox());
			if(e == player) continue;
			else if(intersects) break;
		}
		
		return intersects;
	}
	
	public Rectangle hitBox() {
		return new Rectangle(this.getX(), this.getY(), BULLET_WIDTH, BULLET_HEIGHT);
	}
}
