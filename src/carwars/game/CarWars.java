package carwars.game;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class CarWars extends BasicGame {

	public CarWars(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// TODO Auto-generated method stub
		Input input = container.getInput();
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.drawString("Hello World!", 50, 50);
	}

}
