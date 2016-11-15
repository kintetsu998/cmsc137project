package carwars.game;

import java.awt.Font;

import org.newdawn.slick.Animation;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;

import carwars.chat.Client;
import carwars.chat.UDPClient;
import carwars.model.Player;
import carwars.model.Terrain;
import carwars.util.Config;

public class CarWars extends BasicGame {
	public int[][] terrainMap;
	public SpriteSheet terrain;
	public Player player;
	public Image marker;
	public TrueTypeFont ttf;
	private String username;
	private boolean shooting;
	
	private Client client;
	private UDPClient udpClient;
	
	public CarWars(String title, Client c) {
		super(title);
		this.username = c.getName();
		this.client = c;
	}
	
	public CarWars(String title) {
		super(title);
		this.username = "Player 1";
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		SpriteSheet p1Sheet = new SpriteSheet("resource/car1-sprites.png", 40, 30);
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
		udpClient = new UDPClient();
		
		try{
			String statuses = udpClient.receive();
			initStatuses(statuses, username);
		} catch(Exception e) {
			e.printStackTrace();
			
			player = new Player(this.username, 
					new Animation(p1Sheet, Config.ANIM_SPEED), 
					50, 
					client, 
					null
			);
		}
		
		terrainMap = Terrain.loadTerrain();
		terrain = new SpriteSheet("resource/land-rescale.png", 
				Terrain.TERR_SIZE, Terrain.TERR_SIZE);
		ttf = new TrueTypeFont(font, true);
		marker = new Image("resource/angle-rescale.png");
		
		for(int i=0, mapI=0; i<Config.MAP_HEIGHT; i++, mapI+=Terrain.TERR_SIZE) {
			for(int j=0, mapJ=0; j<Config.MAP_WIDTH; j++, mapJ+=Terrain.TERR_SIZE) {
				if(terrainMap[i][j] <= 0) continue;
				else {
					new Terrain(terrain.getSubImage(terrainMap[i][j]-1, 0), mapJ, mapI);
				}
			}
		}
		
		for(final Player p : Player.players) {
			new Thread() {
				public void run() {
					p.fall();
				}
			}.start();
		}
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		
		shooting = false;
		
		for(Player p : Player.players) {
			p.getSpriteAnim().update(delta);
			
			if(input.isKeyDown(Input.KEY_LEFT) && p.isTurn()) {
				p.moveLeft();
			} else if(input.isKeyDown(Input.KEY_RIGHT) && p.isTurn()) {
				p.moveRight();
			} else if(input.isKeyDown(Input.KEY_UP)) {
				p.incAngle();
			} else if(input.isKeyDown(Input.KEY_DOWN)) {
				p.decAngle();
			} else if(input.isKeyDown(Input.KEY_SPACE)) {
				shooting = true;
				p.incForce();
			}
		}
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		setFont(g);
		renderTerrain(g);
		for(Player p : Player.players) {
			renderPlayer(p, g);
		}
	}
	
	private void initStatuses(String msg, String username) throws SlickException{
		SpriteSheet p1Sheet = new SpriteSheet("resource/car1-sprites.png", 40, 30);
		String[] statuses = msg.split(",");
		
		System.out.println(statuses[0]);
		
		for(String status : statuses) {
			if(!status.trim().equals("")){
				String[] tok = status.trim().split(" ");
				
				System.out.println("0" + tok[0]);
				System.out.println("1" + tok[1]);
				
				if(tok[0].equals(username)) {
					player = new Player(tok[0], 
							new Animation(p1Sheet, Config.ANIM_SPEED),
							Integer.parseInt(tok[1]),
							client,
							udpClient
					);
				} else {
					new Player(tok[0], 
							new Animation(p1Sheet, Config.ANIM_SPEED),
							Integer.parseInt(tok[1]),
							client,
							udpClient
					);
				}
			}
		}
	}
	
	private void setFont(Graphics g) {
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
		Image markerCopy;
		
		g.setColor(Color.black);
		if(p.getFront() == Player.RIGHT) {
			p.getSpriteAnim().draw(p.getX(), p.getY());
			markerCopy = marker.copy();
			markerCopy.rotate(-1 * p.getAngle());
			markerCopy.draw(p.getX()-Player.CAR_WIDTH*2/3, p.getY() + Player.CAR_HEIGHT/4);
			g.drawString(Integer.toString(p.getAngle()), p.getX() + Player.CAR_WIDTH, p.getY());
		} else {
			p.getSpriteAnim().getCurrentFrame().getFlippedCopy(true, false).draw(p.getX(), p.getY());
			markerCopy = marker.getFlippedCopy(true,false);
			markerCopy.rotate(p.getAngle());
			markerCopy.draw(p.getX()-Player.CAR_WIDTH, p.getY() + Player.CAR_HEIGHT/4);
			g.drawString(Integer.toString(p.getAngle()), p.getX()-Player.CAR_WIDTH/5, p.getY());
		}
		
		g.drawString(p.getName(), p.getX(), p.getY() - 15);
		g.fillRect(p.getX()-1, p.getY() + Player.CAR_HEIGHT-1, Player.CAR_WIDTH+2, 7);
		
		g.setColor(Color.green);
		g.fillRect(p.getX(), p.getY() + Player.CAR_HEIGHT, remainingHP(p), 5);
		
		g.setColor(Color.white);
		g.fillRect(p.getX(), p.getY() + Player.CAR_HEIGHT + 14, remainingMov(p), 5);
		
		if(shooting) {
			g.setColor(Color.orange);
			g.fillRect(p.getX(), p.getY() + Player.CAR_HEIGHT + 7, remainingForce(p), 5);
		}
	}
	
	private float remainingHP(Player p) {
		return getBarWidth(p.getHP(), Player.MAX_HP, Player.CAR_WIDTH);
	}
	
	private float remainingMov(Player p) {
		return getBarWidth(p.getMovement(), Player.CAR_MAX_DIST, Player.CAR_WIDTH);
	}
	
	private float remainingForce(Player p) {
		return getBarWidth(p.getForce(), Player.MAX_FORCE, Player.CAR_WIDTH);
	}
	
	private float getBarWidth(float rem, float max, float width) {
		return Math.max(((float) rem/max)*width, 0);
	}
}
