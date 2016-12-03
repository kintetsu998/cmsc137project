package carwars.game;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JOptionPane;

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
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.gui.TextField;

import carwars.chat.TCPClient;
import carwars.chat.UDPClient;
import carwars.model.Bullet;
import carwars.model.Player;
import carwars.model.Terrain;
import carwars.util.Code;
import carwars.util.Config;
import carwars.util.Resources;
import carwars.util.Settings;

public class CarWars extends BasicGame {
	public static final int CLOUDS = Integer.parseInt(Settings.getInstance().getProperty("game.clouds"));
	
	private SpriteSheet terrain;
	private TrueTypeFont ttf;
	
	private Image marker;
	private Image sun;
	private Image cloud;
	private Image bullet;
	
	private TextField chatBox;
	
	private Player player;
	private TCPClient client;
	private UDPClient udpClient;
	
	private String username;
	private Random rand;
	
	private Point[] cloudPoints;
	private Point sunPoint;
	
	private ArrayList<String> messages;
	
	private boolean chatting;
	private boolean oneWon;
	private int[][] terrainMap;
	
	public CarWars(String title, TCPClient c) {
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
		rand = new Random();
		
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
		terrain = new SpriteSheet(Resources.TERRAIN_SPRITE, 
				Terrain.TERR_SIZE, Terrain.TERR_SIZE);
		ttf = new TrueTypeFont(font, true);
		
		marker = new Image(Resources.MARKER);
		bullet = new Image(Resources.BULLET);
		
		initWeather(CLOUDS);
		
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
		oneWon = false;
		
		new Thread() {
			@Override
			public void run() {
				player.fall();
			}
		}.start();
	}
	
	private void initWeather(int n) throws SlickException{
		int x = rand.nextInt(50);
		int y = rand.nextInt(40);
		
		sun = new Image(Resources.SUN);
		cloud = new Image(Resources.CLOUD);
		
		sunPoint = new Point((Config.GAME_WIDTH*4)/5, 30);
		
		cloudPoints = new Point[n];
		for(int i = 0; i < n; i++) {
			cloudPoints[i] = new Point(x, y);
			x = (x + rand.nextInt(Config.GAME_WIDTH/n)) % Config.GAME_WIDTH;
			y = (y + rand.nextInt(50) + 30) % 100;
		}
		
		new Thread() {
			@Override
			public void run() {
				try{
					while(true) {
						for(Point p : cloudPoints) {
							p.setX((p.getX() - 1) % Config.GAME_WIDTH);
							Thread.sleep(100);
						}
					}
				} catch(Exception e) {
					Thread.currentThread().interrupt();
				}
			}
		}.start();
		
		new Thread() {
			@Override
			public void run() {
				try{
					while(true) {
						sunPoint.setX((sunPoint.getX() - 1) % Config.GAME_WIDTH);
						Thread.sleep(6000);
					}
				} catch(Exception e) {
					Thread.currentThread().interrupt();
				}
			}
		}.start();
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		
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
				player.shoot(udpClient);
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
		
		for(Player p : Player.players.values()) {
			p.getSpriteAnim().update(delta);
		}
		
		chatBox.setFocus(chatting);
		udpClient.sendStatus();
		
		if(aliveCount() <= 1) {
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch(Exception e) {
						return;
					}
					
					if(aliveCount() > 1) {
						return;
					} else {
						oneWon = true;
					}
				}
			}.start();
		}
		
		if(oneWon) {
			String name = getAlive();
			if(name != null) {
				JOptionPane.showMessageDialog(
						null, 
						name + " won the game!", 
						"Game End", 
						JOptionPane.DEFAULT_OPTION
				);
			} else {
				JOptionPane.showMessageDialog(
						null, 
						"It is a tie", 
						"Game End", 
						JOptionPane.DEFAULT_OPTION
				);
			}
			
			Settings.getInstance().saveProperty();
			System.exit(0);
		}
	}
	
	private int aliveCount() {
		int count = 0;
		for(Player p: Player.players.values()){
			if(!p.isDead()) count++;
		}
		
		return count;
	}
	
	private String getAlive() {
		for(String name: Player.players.keySet()){
			if(!Player.players.get(name).isDead()) 
				return name;
		}
		
		return null;
	}

	private int getPlayerAngle(Player p, int x, int y) {
		int xdist = (int) p.getX()-x;
		int ydist = (int) (p.getY()-y) * -1;
		float angle = (float) Math.toDegrees(Math.atan((float) ydist/xdist));
		
		return (int) Math.floor(angle);
	}
	
	private float getPlayerForce(Player p, int x, int y) {
		int xdist = (int) p.getX()-x;
		int ydist = (int) p.getY()-y;
		float force = (float) Math.sqrt(xdist*xdist + ydist*ydist);
		
		return (force/Config.GAME_WIDTH) * 20.0f;
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		setFont(g);
		renderTerrain(g);
		renderWeather(g);
		renderBullet(g);
		
		for(Player p : Player.players.values()) {
			renderPlayer(p, g);
		}
		
		
		if(chatting){
			chatBox.render(container, g);
			g.setColor(new Color(0.6f, 0.6f, 0.6f, 0.5f));
			g.fillRect(0, 0, Config.GAME_WIDTH/2, 100);
		}
		
		renderChat(g);
		
		if(Config.DEBUG) {
			g.setColor(Color.red);
			g.draw(player.hitBox());
			g.draw(player.leftHitBox());
			g.draw(player.rightHitBox());
			g.draw(player.topHitBox());
		}
	}
	
	private void renderWeather(Graphics g) {
		sun.draw(sunPoint.getX(), sunPoint.getY());
		for(Point p : cloudPoints) {
			cloud.draw(p.getX(), p.getY());
		}
	}
	
	private void renderBullet(Graphics g) {
		Image rotatedBullet;
		
		g.setColor(Color.red);
		for(int i=0; i<Bullet.bullets.size(); i++) {
			Bullet b = Bullet.bullets.get(i);
			
			rotatedBullet = bullet.getFlippedCopy(b.getFront() == Player.LEFT, false);
			rotatedBullet.rotate(b.getRotate());
			rotatedBullet.draw(b.getX(), b.getY());
			
			if(Config.DEBUG) {
				g.draw(b.hitBox());
			}
		}
	}

	private void initStatuses(String msg, String username) throws SlickException{
		ArrayList<SpriteSheet> playerSprites = new ArrayList<>();
		String[] statuses = msg.replace(Code.GET_ALL_STATUS, "").split(",");
		int i=0;
		
		Animation deadCar = new Animation(
				new SpriteSheet(
						Resources.DEAD_CAR, 
						Player.CAR_WIDTH, 
						Player.CAR_HEIGHT+4
				), 
				Config.ANIM_SPEED
		);
		
		initPlayerSprites(playerSprites);
		
		try{
			for(String status : statuses) {
				try {
					if(!status.trim().equals("")){
						String[] tok = status.trim().split(" ");
						
						if(tok[0].equals(username)) {
							player = new Player(tok[0], 
									new Animation(playerSprites.get(i), Config.ANIM_SPEED),
									deadCar,
									Float.parseFloat(tok[1]),
									client
							);
						} else {
							new Player(tok[0], 
									new Animation(playerSprites.get(i), Config.ANIM_SPEED),
									deadCar,
									Float.parseFloat(tok[1]),
									client
							);
						}
						i++;
					}
				} catch(NumberFormatException e) {
					e.printStackTrace();
					System.out.println(status);
				}
			}
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	private void initPlayerSprites(ArrayList<SpriteSheet> playerSprite) throws SlickException{
		for(int i=1; i <= Config.MAX_PLAYERS; i++) {
			String filename = Resources.CAR_SPRITE.replace("I", Integer.toString(i));
			
			playerSprite.add(new SpriteSheet(filename, Player.CAR_WIDTH, Player.CAR_HEIGHT));
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
					p.update(Float.parseFloat(tok[1]),
							 Float.parseFloat(tok[2]),
							 Integer.parseInt(tok[3]),
							 Integer.parseInt(tok[4]),
							 Integer.parseInt(tok[5]));
				}
			}
		}
	}
	
	private void setFont(Graphics g) {
		g.setFont(ttf);
	}
	
	private void renderTerrain(Graphics g) {
		g.setBackground(new Color(224, 247, 250));
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
			if(!p.isDead()) {
				markerCopy = marker.copy();
				markerCopy.rotate(p.getAngle() * -1);
				markerCopy.draw(p.getX()-Player.CAR_WIDTH*2/3, p.getY() + Player.CAR_HEIGHT/4);
			}
			p.getSpriteAnim().getCurrentFrame().draw(p.getX(), p.getY());
		} else {
			if(!p.isDead()) {
				markerCopy = marker.getFlippedCopy(true,false);
				markerCopy.rotate(p.getAngle() * -1);
				markerCopy.draw(p.getX()-Player.CAR_WIDTH, p.getY() + Player.CAR_HEIGHT/4);
			}
			p.getSpriteAnim().getCurrentFrame().getFlippedCopy(true, false).draw(p.getX(), p.getY());
		}
		
		if(!p.isDead()) {
			g.drawString(p.getName(), p.getX(), p.getY() - 15);
			g.fillRect(p.getX()-1, p.getY() + Player.CAR_HEIGHT-1, Player.CAR_WIDTH+2, 7);
			
			g.setColor(Color.green);
			g.fillRect(p.getX(), p.getY() + Player.CAR_HEIGHT, remainingHP(p), 5);
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
	
	private float getBarWidth(float rem, float max, float width) {
		return Math.max(((float) rem/max)*width, 0);
	}
}
