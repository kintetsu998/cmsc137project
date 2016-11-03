package carwars.model;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

public class Entity {
	public static final ArrayList<Entity> entities = new ArrayList<>();
	
	private float x;
	private float y;
	
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
		
		entities.add(this);
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	protected float setX(float x) {
		return (this.x = x);
	}
	
	protected float setY(float y) {
		return (this.y = y);
	}
	
	public Rectangle hitBox() {
		if(this instanceof Player)
			return new Rectangle(this.getX(), this.getY(), Player.CAR_WIDTH, Player.CAR_HEIGHT);
		else
			return new Rectangle(this.getX(), this.getY(), Terrain.TERR_SIZE, Terrain.TERR_SIZE);
	}
}
