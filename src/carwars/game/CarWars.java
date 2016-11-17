package carwars.game;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;

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
import org.newdawn.slick.gui.TextField;

import carwars.chat.Client;
import carwars.chat.UDPClient;
import carwars.model.Player;
import carwars.model.Terrain;
import carwars.util.Code;
import carwars.util.Config;

public class CarWars extends BasicGame {
	public int[][] terrainMap;
	public SpriteSheet terrain;
	public Player player;
	public Image marker;
	public TrueTypeFont ttf;
	private String username;
	
	private Client client;
	private UDPClient udpClient;
	
	private TextField chatBox;
	
	//private boolean shooting;
	
	private boolean chatting;
	
	private ArrayList<String> messages;
	
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
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
		udpClient = new UDPClient(this);
		messages = new ArrayList<>(Arrays.asList("","","",""));
		
		try{
			String statuses = udpClient.receive();
			initStatuses(statuses, username);
			udpClient.setPlayer(player);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		udpClient.start();
		client.setGame(this);
		
		terrainMap = Terrain.loadTerrain();
		terrain = new SpriteSheet("resource/land-rescale.png", 
				Terrain.TERR_SIZE, Terrain.TERR_SIZE);
		ttf = new TrueTypeFont(font, true);
		marker = new Image("resource/angle-rescale.png");
		chatBox = new TextField(container, ttf, 10, 80, 200, 15);
		
		for(int i=0, mapI=0; i<Config.MAP_HEIGHT; i++, mapI+=Terrain.TERR_SIZE) {
			for(int j=0, mapJ=0; j<Config.MAP_WIDTH; j++, mapJ+=Terrain.TERR_SIZE) {
				if(terrainMap[i][j] <= 0) continue;
				else {
					new Terrain(terrain.getSubImage(terrainMap[i][j]-1, 0), mapJ, mapI);
				}
			}
		}
		
		chatting = false;
		
		new Thread() {
			@Override
			public void run() {
				player.fall();
			}
		}.start();
		
		/*new Thread() {
			@Override
			public void run() {
				while(true) {
					updateChat(messages.get(0));
					try{
						Thread.sleep(1000);
					} catch(Exception e){
						Thread.currentThread().interrupt();
					}
				}
			}
		}.start();*/
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		
		//shooting = false;
		
		if(!chatting) {
			if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
				player.moveLeft();
			} else if(input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
				player.moveRight();
			} else if(input.isKeyPressed(Input.KEY_ENTER)) {
				chatting = true;
			}
			
			if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
				player.jump();
			}
			
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				player.shoot();
			}
			
			player.setAngle(getPlayerAngle(player, input.getMouseX(), input.getMouseY()));
			player.setForce(getPlayerForce(player, input.getMouseX(), input.getMouseY()));
		} else {
			if(input.isKeyPressed(Input.KEY_ENTER)) {
				String str = new String(chatBox.getText());
				
				chatting = false;
				if(!str.trim().equals("")) {
					client.sendMessage(str);
				}
				chatBox.setText("");
			} else if(input.isKeyPressed(Input.KEY_ESCAPE)) {
				chatting = false;
				chatBox.setText("");
			}
		}
		
		/* else if(input.isKeyDown(Input.KEY_UP)) {
			player.incAngle();
		} else if(input.isKeyDown(Input.KEY_DOWN)) {
			player.decAngle();
		} else if(input.isKeyDown(Input.KEY_SPACE)) { //&& player.isTurn()) {
			shooting = true;
			player.incForce();
		}*/
		
		for(Player p : Player.players.values()) {
			p.getSpriteAnim().update(delta);
		}
		
		chatBox.setFocus(chatting);
		udpClient.sendStatus();
	}
	
	private float getPlayerAngle(Player p, int x, int y) {
		int xdist = (int) p.getX()-x;
		int ydist = (int) (p.getY()-y) * -1;
		float angle = (float) Math.toDegrees(Math.atan((float) ydist/xdist));
		
		return angle;
	}
	
	private float getPlayerForce(Player p, int x, int y) {
		int xdist = (int) p.getX()-x;
		int ydist = (int) p.getY()-y;
		float force = (float) Math.sqrt(xdist*xdist + ydist*ydist);
		
		return force;
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		setFont(g);
		renderTerrain(g);
		
		for(Player p : Player.players.values()) {
			if(!p.isDead()) {
				renderPlayer(p, g);
			}
		}
		
		if(chatting){
			g.setColor(Color.lightGray);
			g.fillRect(0, 0, Config.GAME_WIDTH/2, 100);
			chatBox.render(container, g);
		}
		
		renderAddInfo(player, g);
		renderChat(g);
	}
	
	private void initStatuses(String msg, String username) throws SlickException{
		ArrayList<SpriteSheet> playerSprites = new ArrayList<>();
		String[] statuses = msg.replace(Code.GET_ALL_STATUS, "").split(",");
		int i=0;
		
		initPlayerSprites(playerSprites);
		
		for(String status : statuses) {
			if(!status.trim().equals("")){
				String[] tok = status.trim().split(" ");
				
				if(tok[0].equals(username)) {
					player = new Player(tok[0], 
							new Animation(playerSprites.get(i), Config.ANIM_SPEED),
							Integer.parseInt(tok[1]),
							client
					);
				} else {
					new Player(tok[0], 
							new Animation(playerSprites.get(i), Config.ANIM_SPEED),
							Integer.parseInt(tok[1]),
							client
					);
				}
				i++;
			}
		}
	}
	
	private void initPlayerSprites(ArrayList<SpriteSheet> playerSprite) throws SlickException{
		for(int i=1; i <= Config.MAX_PLAYERS; i++) {
			String filename = "resource/sprites/car" 
					+ Integer.toString(i)
					+ "-sprites.png";
			
			playerSprite.add(new SpriteSheet(filename, 40, 30));
		}
	}
	
	public void updateStatuses(String msg, String name) throws SlickException{
		String[] statuses = msg.replace(Code.GET_ALL_STATUS, "").trim().split(",");
		
		if(Player.players.size() <= 0) {
			return;
		}
		
		for(String status : statuses) {
			if(!status.trim().equals("")){
				String[] tok = status.trim().split(" ");
				
				if(!name.equals(tok[0])) {
					Player p = Player.players.get(tok[0]);
					p.update(Integer.parseInt(tok[1]),
							 Integer.parseInt(tok[2]),
							 Integer.parseInt(tok[3]),
							 Integer.parseInt(tok[4]));
				}
			}
		}
	}
	
	private void setFont(Graphics g) {
		g.setFont(ttf);
	}
	
	private void renderTerrain(Graphics g) {
		g.setBackground(Color.cyan);
		terrain.startUse();
		for(Terrain t : Terrain.terrains) {
			t.getSprite().drawEmbedded(t.getX(), t.getY(), Terrain.TERR_SIZE, Terrain.TERR_SIZE);
		}
		terrain.endUse();
	}

	private void renderPlayer(Player p, Graphics g) {
		g.setColor(Color.black);
		if(p.getFront() == Player.RIGHT) {
			p.getSpriteAnim().draw(p.getX(), p.getY());
		} else {
			p.getSpriteAnim().getCurrentFrame().getFlippedCopy(true, false).draw(p.getX(), p.getY());
		}
		
		g.drawString(p.getName(), p.getX(), p.getY() - 15);
		g.fillRect(p.getX()-1, p.getY() + Player.CAR_HEIGHT-1, Player.CAR_WIDTH+2, 7);
		
		g.setColor(Color.green);
		g.fillRect(p.getX(), p.getY() + Player.CAR_HEIGHT, remainingHP(p), 5);
	}
	
	private void renderAddInfo(Player p, Graphics g) {
		Image markerCopy;
		/*g.setColor(Color.white);
		g.fillRect(p.getX(), p.getY() + Player.CAR_HEIGHT + 14, remainingMov(p), 5);*/
		
		g.setColor(Color.black);
		if(p.getFront() == Player.RIGHT) {
			markerCopy = marker.copy();
			markerCopy.rotate(p.getAngle() * -1);
			markerCopy.draw(p.getX()-Player.CAR_WIDTH*2/3, p.getY() + Player.CAR_HEIGHT/4);
			//g.drawString(Integer.toString(p.getAngle()), p.getX() + Player.CAR_WIDTH, p.getY());
		} else {
			markerCopy = marker.getFlippedCopy(true,false);
			markerCopy.rotate(p.getAngle() * -1);
			markerCopy.draw(p.getX()-Player.CAR_WIDTH, p.getY() + Player.CAR_HEIGHT/4);
			//g.drawString(Integer.toString(p.getAngle()), p.getX()-Player.CAR_WIDTH/5, p.getY());
		}
		
		/*if(shooting) {
			g.setColor(Color.orange);
			g.fillRect(p.getX(), p.getY() + Player.CAR_HEIGHT + 7, remainingForce(p), 5);
		}*/
		
		/*if(chatting) {
			g.drawString("Chatting...", 10, 80);
		}*/
		
		if(Config.DEBUG) {
			g.setColor(Color.red);
			g.draw(p.hitBox());
			g.draw(p.leftHitBox());
			g.draw(p.rightHitBox());
		}
	}
	
	private void renderChat(Graphics g) {
		int y = 10;
		
		g.setColor(Color.black);
		for(int i=0; i<messages.size(); i++) {
			g.drawString(messages.get(i), 10, y);
			y += 15;
		}
	}
	
	public void updateChat(String msg) {
		messages.remove(0);
		messages.add(msg);
	}
	
	private float remainingHP(Player p) {
		return getBarWidth(p.getHP(), Player.MAX_HP, Player.CAR_WIDTH);
	}
	
	/*private float remainingMov(Player p) {
		return getBarWidth(p.getMovement(), Player.CAR_MAX_DIST, Player.CAR_WIDTH);
	}
	
	private float remainingForce(Player p) {
		return getBarWidth(p.getForce(), Player.MAX_FORCE, Player.CAR_WIDTH);
	}*/
	
	private float getBarWidth(float rem, float max, float width) {
		return Math.max(((float) rem/max)*width, 0);
	}
}
