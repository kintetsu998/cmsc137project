package carwars.model;

import java.util.ArrayList;

import org.newdawn.slick.Image;

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
}
