package carwars.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {
	static public final int SPRITE_SIZE = 20;
	static public final int GAME_WIDTH = 800;
	static public final int GAME_HEIGHT = 600;
	static public final int TER_WIDTH = GAME_WIDTH/SPRITE_SIZE;
	static public final int TER_HEIGHT = GAME_HEIGHT/SPRITE_SIZE;
	
	public static int[][] loadTerrain() {
		int[][] terrain = new int[TER_HEIGHT][TER_WIDTH];
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
		}catch(IOException e) {
			System.err.println("File not found.");
			System.exit(1);
		} finally {
			return terrain;
		}
	}
}
