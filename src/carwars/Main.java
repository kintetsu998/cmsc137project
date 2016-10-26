package carwars;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import carwars.game.CarWars;

public class Main {
	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new CarWars("Car Wars"));
			app.setDisplayMode(1280, 960, false);
			app.setAlwaysRender(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
