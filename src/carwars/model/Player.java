package carwars.model;

import org.newdawn.slick.Image;

public class Player extends Entity{
	private String name;
	
	public Player(String name, Image sprite, int x, int y) {
		super(sprite, x, y);
		this.name = name;
	}
	
	public Player(String name, Image sprite) {
		super(sprite, 0, 0);
		this.name = name;
	}
	
	public Player(String name, String sprite_file, int x, int y) {
		super(sprite_file, x, y);
		this.name = name;
	}
	
	public Player(String name, String sprite_file) {
		super(sprite_file, 0, 0);
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
