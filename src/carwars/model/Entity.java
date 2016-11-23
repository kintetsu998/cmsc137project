package carwars.model;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

public abstract class Entity {
	public static final ArrayList<Entity> entities = new ArrayList<>();
	
	private float x;
	private float y;
	
	public Entity(float x, float y) {
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
	
	public float setX(float x) {
		return (this.x = x);
	}
	
	public float setY(float y) {
		return (this.y = y);
	}
	
	public abstract Rectangle hitBox();
}
