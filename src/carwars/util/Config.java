package carwars.util;

import carwars.model.Terrain;

public class Config {
	static public final boolean DEBUG = Boolean.parseBoolean(Settings.getInstance().getProperty("game.debug"));
	
	static public final int GAME_WIDTH = Integer.parseInt(Settings.getInstance().getProperty("game.width"));
	static public final int GAME_HEIGHT = Integer.parseInt(Settings.getInstance().getProperty("game.height"));
	
	static public final int MAP_WIDTH = GAME_WIDTH/Terrain.TERR_SIZE;
	static public final int MAP_HEIGHT = GAME_HEIGHT/Terrain.TERR_SIZE;
	
	static public final int GRAVITY = Integer.parseInt(Settings.getInstance().getProperty("game.gravity"));
	static public final int TERMINAL_SPEED = Integer.parseInt(Settings.getInstance().getProperty("game.terminal_speed"));
	
	static public final int ANIM_SPEED = Integer.parseInt(Settings.getInstance().getProperty("game.anim_speed"));
	
	static public final int BUFFER_SIZE = Integer.parseInt(Settings.getInstance().getProperty("udp.buffer_size"));
	static public final int UDP_SERVER_PORT = Integer.parseInt(Settings.getInstance().getProperty("udp.server_port"));
	static public final int UDP_CLIENT_PORT = Integer.parseInt(Settings.getInstance().getProperty("udp.client_port"));
	static public final int TCP_PORT = Integer.parseInt(Settings.getInstance().getProperty("tcp.port"));
	
	static public final String UDP_SERVER_IP = Settings.getInstance().getProperty("udp.server_ip");
	static public final String TCP_SERVER_IP = Settings.getInstance().getProperty("tcp.server_ip");
	
	static public final int MAX_PLAYERS = Integer.parseInt(Settings.getInstance().getProperty("game.max_player"));
	static public final int MIN_PLAYERS = Integer.parseInt(Settings.getInstance().getProperty("game.min_player"));
}
