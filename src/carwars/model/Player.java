package carwars.model;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import carwars.util.Config;

public class Player {
	private String name;
	private Image sprite;
	
	private int x;
	private int y;
	
	public Player(String name, Image sprite, int x, int y) {
		this.name = name;
		this.sprite = sprite;
		this.x = x;
		this.y = y;
	}
	
	public Player(String name, Image sprite) {
		this.name = name;
		this.sprite = sprite;
		this.x = 0;
		this.y = 0;
	}
	
	public Player(String name, String sprite_file, int x, int y) {
		this.name = name;
		try {
			this.sprite = new Image(sprite_file);
		} catch (SlickException e) {
			//TODO show window that shows image not found
			e.printStackTrace();
			this.sprite = null;
		}
		this.x = x;
		this.y = y;
	}
	
	public Player(String name, String sprite_file) {
		this.name = name;
		try {
			this.sprite = new Image(sprite_file);
		} catch (SlickException e) {
			//TODO show window that shows image not found
			e.printStackTrace();
			this.sprite = null;
		}
		this.x = 0;
		this.y = 0;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Image getSprite() {
		return this.sprite;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Rectangle hitBox() {
		return new Rectangle(Config.SPRITE_SIZE, Config.SPRITE_SIZE, x, y);
	}
}
