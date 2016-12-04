package carwars.util;

public class Resources {
	public static final String ASSETS_DIR 	  = Settings.getInstance().getProperty("assets.dir");
	public static final String DEAD_CAR		  = ASSETS_DIR + Settings.getInstance().getProperty("assets.dead_car");
	public static final String TERRAIN_SPRITE = ASSETS_DIR + Settings.getInstance().getProperty("assets.terrain_sprite");
	public static final String TERRAIN_TXT	  = ASSETS_DIR + Settings.getInstance().getProperty("assets.terrain_txt");
	public static final String BARREL		  = ASSETS_DIR + Settings.getInstance().getProperty("assets.barrel");
	public static final String MARKER		  = ASSETS_DIR + Settings.getInstance().getProperty("assets.marker");
	public static final String BULLET		  = ASSETS_DIR + Settings.getInstance().getProperty("assets.bullet");
	public static final String SUN		 	  = ASSETS_DIR + Settings.getInstance().getProperty("assets.sun");
	public static final String CLOUD		  = ASSETS_DIR + Settings.getInstance().getProperty("assets.cloud");
	public static final String CAR_SPRITE	  = ASSETS_DIR + Settings.getInstance().getProperty("assets.car_sprite");
	public static final String PANEL_BG		  = ASSETS_DIR + Settings.getInstance().getProperty("assets.panel_bg");
	public static final String CONTROL_BG	  = ASSETS_DIR + Settings.getInstance().getProperty("assets.control_bg");
}
