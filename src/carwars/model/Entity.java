package carwars.model;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import carwars.util.Config;

public class Entity {
	public static final ArrayList<Entity> entities = new ArrayList<>();
	
	private Image sprite;
	private int x;
	private int y;
	
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
		}
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
	
	public Rectangle hitBox() {
		return new Rectangle(Config.SPRITE_SIZE, Config.SPRITE_SIZE, x, y);
	}
	
	public Image getSprite() {
		return this.sprite;
	}
}
