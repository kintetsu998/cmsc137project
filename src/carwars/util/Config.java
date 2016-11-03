package carwars.util;

import carwars.model.Terrain;

public class Config {
	
	static public final int GAME_WIDTH = 800;
	static public final int GAME_HEIGHT = 600;
	
	static public final int MAP_WIDTH = GAME_WIDTH/Terrain.TERR_SIZE;
	static public final int MAP_HEIGHT = GAME_HEIGHT/Terrain.TERR_SIZE;
	
	static public final float GRAVITY = 6.5f;
	static public final int GRAVITY_SPEED = 20;
	static public final int TERMINAL_SPEED = 7;
	
	static public final int ANIM_SPEED = 250;
}
