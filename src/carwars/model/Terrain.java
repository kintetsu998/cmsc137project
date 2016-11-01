package carwars.model;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import carwars.util.Config;

public class Terrain extends Entity {
	public static final ArrayList<Terrain> terrains = new ArrayList<>();
	
	public Terrain(Image sprite, int x, int y) {
		super(sprite, x, y);
		terrains.add(this);
	}
	
	public Terrain(String sprite, int x, int y) {
		super(sprite, x, y);
		terrains.add(this);
	}
	
	public Rectangle hitBox() {
		return new Rectangle(this.getX(), this.getY(), Config.TERR_SIZE, Config.TERR_SIZE);
	}
}
