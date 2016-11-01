package carwars.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {
	static public final int TERR_SIZE = 20;
	
	static public final int CAR_WIDTH = 40;
	static public final int CAR_HEIGHT = 30;
	
	static public final int GAME_WIDTH = 800;
	static public final int GAME_HEIGHT = 600;
	
	static public final int MAP_WIDTH = GAME_WIDTH/TERR_SIZE;
	static public final int MAP_HEIGHT = GAME_HEIGHT/TERR_SIZE;
	
	static public final float GRAVITY = 0.75f;
	static public final int GRAVITY_SPEED = 20;
	static public final int TERMINAL_SPEED = 7;
	
	public static int[][] loadTerrain() {
		int[][] terrain = new int[MAP_HEIGHT][MAP_WIDTH];
		try{
			BufferedReader br = new BufferedReader(new FileReader("resource/terrain.txt"));
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
			System.exit(1);
		}
		
		return terrain;
	}
}
