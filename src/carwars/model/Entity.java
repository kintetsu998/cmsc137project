package carwars.model;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

public abstract class Entity {
	public static final ArrayList<Entity> entities = new ArrayList<>();
	
	private int x;
	private int y;
	
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
		
		entities.add(this);
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int setX(int x) {
		return (this.x = x);
	}
	
	public int setY(int y) {
		return (this.y = y);
	}
	
	public abstract Rectangle hitBox();
}
