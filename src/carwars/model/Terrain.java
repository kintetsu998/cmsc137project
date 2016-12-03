package carwars.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import carwars.util.Config;
import carwars.util.Resources;
import carwars.util.Settings;

public class Terrain extends Entity {
	public static final ArrayList<Terrain> terrains = new ArrayList<>();
	static public final int TERR_SIZE = Integer.parseInt(Settings.getInstance().getProperty("terrain.size"));
	
	private Image sprite;
	
	public Terrain(Image sprite, int x, int y) {
		super(x, y);
		this.sprite = sprite;
		terrains.add(this);
	}
	
	public static int[][] loadTerrain(int mapID) {
		int[][] terrain = new int[Config.MAP_HEIGHT][Config.MAP_WIDTH];
		try{
			BufferedReader br = new BufferedReader(
					new FileReader(
							Resources.TERRAIN_TXT.replace("I", Integer.toString(mapID))
						)
					);
			
			String line;
			int i=0;
			
			while((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				for(int j=0; j<tokens.length; j++) {
					terrain[i][j] = Integer.parseInt(tokens[j]);
				}
				
				i++;
			}
			
			br.close();
		}catch(IOException e) {
			System.err.println("File not found.");
			e.printStackTrace();
			System.exit(1);
		}
		
		return terrain;
	}
	
	public Image getSprite() {
		return this.sprite;
	}
	
	@Override
	public Rectangle hitBox() {
		return new Rectangle(this.getX(), this.getY(), Terrain.TERR_SIZE, Terrain.TERR_SIZE);
	}
}
