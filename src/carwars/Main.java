package carwars;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import carwars.chat.PlayerLogin;
import carwars.game.CarWars;

public class Main {
	public static void main(String[] args) {
		new PlayerLogin("localhost", 1500);
	}
	
	public static void launchApp(String username){
		try {
			AppGameContainer app = new AppGameContainer(new CarWars("Car Wars", username));
			app.setDisplayMode(800, 600, false); //create 800x600 frame
			app.setTargetFrameRate(60); //cap FPS to 60
			//app.setShowFPS(false);
			app.setAlwaysRender(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
