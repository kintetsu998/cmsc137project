package carwars.game;

import java.awt.Font;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;

import carwars.model.Player;
import carwars.model.Terrain;
import carwars.util.Config;

public class CarWars extends BasicGame {
	public int[][] terrainMap;
	public SpriteSheet terrain;
	public Player p;
	public Image marker;
	
	public CarWars(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		terrainMap = Terrain.loadTerrain();
		terrain = new SpriteSheet("resource/land-rescale.png", 
				Terrain.TERR_SIZE, Terrain.TERR_SIZE);
		
		p = new Player("Player 1", "resource/car-40.png", 50, 0);
		marker = new Image("resource/angle-rescale.png");
		
		for(int i=0, mapI=0; i<Config.MAP_HEIGHT; i++, mapI+=Terrain.TERR_SIZE) {
			for(int j=0, mapJ=0; j<Config.MAP_WIDTH; j++, mapJ+=Terrain.TERR_SIZE) {
				if(terrainMap[i][j] <= 0) continue;
				else {
					new Terrain(terrain.getSubImage(terrainMap[i][j]-1, 0), mapJ, mapI);
				}
			}
		}
		
		p.moveLeft();
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		if(!Player.intersectsTerrain(p.hitBox())) {
			if(!p.isFalling()) {
				new Thread() {
					public void run() {
						p.fall();
					}
				}.start();
			}
		}
		
		if(input.isKeyDown(Input.KEY_LEFT) && !p.isFalling()) {
			p.moveLeft();
		} else if(input.isKeyDown(Input.KEY_RIGHT) && !p.isFalling()) {
			p.moveRight();
		}
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		setFont(g);
		renderTerrain(g);
		renderPlayer(p, g);
	}
	
	private void setFont(Graphics g) {
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 10);
		TrueTypeFont ttf = new TrueTypeFont(font, true);
		g.setFont(ttf);
	}
	
	private void renderTerrain(Graphics g) {
		g.setBackground(Color.lightGray);
		terrain.startUse();
		for(Terrain t : Terrain.terrains) {
			t.getSprite().drawEmbedded(t.getX(), t.getY(), Terrain.TERR_SIZE, Terrain.TERR_SIZE);
		}
		terrain.endUse();
	}

	
	private void renderPlayer(Player p, Graphics g) {
		marker.rotate(p.getAngle());
		if(p.getFront() == Player.RIGHT) {
			p.getSprite().draw(p.getX(), p.getY());
			marker.draw(p.getX()+Player.CAR_WIDTH/4, p.getY()-Player.CAR_HEIGHT/2);
		} else {
			p.getSprite().getFlippedCopy(true, false).draw(p.getX(), p.getY());
			marker.getFlippedCopy(true,false).draw(p.getX()-Player.CAR_WIDTH, p.getY()-Player.CAR_HEIGHT/2);
		}
		
		g.setColor(Color.black);
		g.drawString(p.getName(), p.getX(), p.getY() - 15);
		g.fillRect(p.getX()-1, p.getY() + Player.CAR_HEIGHT-1, Player.CAR_WIDTH+2, 7);
		
		g.setColor(Color.green);
		g.fillRect(p.getX(), p.getY() + Player.CAR_HEIGHT, remainingHP(p), 5);
	}
	
	private float remainingHP(Player p) {
		return (p.getHP()/Player.MAX_HP)*Player.CAR_WIDTH;
	}
}
