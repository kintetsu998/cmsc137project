package carwars.util;

import carwars.model.Terrain;

public class Config {
	static public final boolean DEBUG = true;
	
	static public final int GAME_WIDTH = 800;
	static public final int GAME_HEIGHT = 600;
	
	static public final int MAP_WIDTH = GAME_WIDTH/Terrain.TERR_SIZE;
	static public final int MAP_HEIGHT = GAME_HEIGHT/Terrain.TERR_SIZE;
	
	static public final int GRAVITY = 6;
	static public final int GRAVITY_SPEED = 20;
	static public final int TERMINAL_SPEED = 7;
	
	static public final int ANIM_SPEED = 250;
	
	static public final int UDP_SERVER_PORT = 4450;
	static public final int UDP_CLIENT_PORT = 4451;
	
	static public final String UDP_SERVER_IP = "230.0.0.1";
	
	static public final int BUFFER_SIZE = 1024;
	
	static public final int MAX_PLAYERS = 8;
	static public final int MIN_PLAYERS = 2;
}
