package carwars.model;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import carwars.util.Config;

public class Entity {
	public static final ArrayList<Entity> entities = new ArrayList<>();
	
	private Image sprite;
	private float x;
	private float y;
	
	public Entity(Image sprite, int x, int y) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		
		entities.add(this);
	}
	
	public Entity(String sprite_file, int x, int y) {
		try {
			this.sprite = new Image(sprite_file);
		} catch (SlickException e) {
			//TODO show window that shows image not found
			e.printStackTrace();
			System.err.println("Sprite file not found: " + sprite_file);
			this.sprite = null;
		} catch (Exception e ) {
			e.printStackTrace();
			System.out.println(this.x + " " + this.y);
		}
		
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
	
	public Image getSprite() {
		return this.sprite;
	}
	
	public Rectangle hitBox() {
		if(this instanceof Player)
			return new Rectangle(this.getX(), this.getY(), Config.CAR_WIDTH, Config.CAR_HEIGHT);
		else
			return new Rectangle(this.getX(), this.getY(), Config.TERR_SIZE, Config.TERR_SIZE);
	}
}
