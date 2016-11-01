package carwars.game;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import carwars.model.Player;
import carwars.model.Terrain;
import carwars.util.Config;

public class CarWars extends BasicGame {
	public int[][] terrainMap;
	public SpriteSheet terrain;
	public Player p;
	
	public CarWars(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		terrainMap = Config.loadTerrain();
		terrain = new SpriteSheet("resource/land-rescale.png", 
				Config.TERR_SIZE, Config.TERR_SIZE);
		
		p = new Player("Player 1", "resource/car-40.png", 50, 50);
		
		for(int i=0, mapI=0; i<Config.MAP_HEIGHT; i++, mapI+=Config.TERR_SIZE) {
			for(int j=0, mapJ=0; j<Config.MAP_WIDTH; j++, mapJ+=Config.TERR_SIZE) {
				if(terrainMap[i][j] <= 0) continue;
				else {
					new Terrain(terrain.getSubImage(terrainMap[i][j]-1, 0), mapJ, mapI);
				}
			}
		}
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		
		if(input.isKeyDown(Input.KEY_LEFT) && !p.isFalling()) {
			p.moveLeft(delta);
		} else if(input.isKeyDown(Input.KEY_RIGHT) && !p.isFalling()) {
			p.moveRight(delta);
		}
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		terrain.startUse();
		for(Terrain t : Terrain.terrains) {
			t.getSprite().drawEmbedded(t.getX(), t.getY(), Config.TERR_SIZE, Config.TERR_SIZE);
		}
		terrain.endUse();
		
		p.getSprite().draw(p.getX(), p.getY());
		
		g.setBackground(Color.lightGray);
	}

}
