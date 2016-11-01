package carwars.game;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import carwars.model.Terrain;
import carwars.util.Config;

public class CarWars extends BasicGame {
	public int[][] terrainMap;
	public SpriteSheet terrain;
	
	public CarWars(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		terrainMap = Config.loadTerrain();
		terrain = new SpriteSheet("resource/land-rescale.png", 
				Config.SPRITE_SIZE, Config.SPRITE_SIZE);
		
		for(int i=0, mapI=0; i<Config.TER_HEIGHT; i++, mapI+=Config.SPRITE_SIZE) {
			for(int j=0, mapJ=0; j<Config.TER_WIDTH; j++, mapJ+=Config.SPRITE_SIZE) {
				if(terrainMap[i][j] <= 0) continue;
				else {
					new Terrain(terrain.getSubImage(terrainMap[i][j]-1, 0), mapJ, mapI);
				}
			}
		}
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// TODO Auto-generated method stub
		Input input = container.getInput();
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		terrain.startUse();
		for(Terrain t : Terrain.terrains) {
			t.getSprite().drawEmbedded(t.getX(), t.getY(), Config.SPRITE_SIZE, Config.SPRITE_SIZE);
			System.out.println(t.getX() + " " + t.getY());
		}
		terrain.endUse();
		
		g.setBackground(Color.lightGray);
	}

}
