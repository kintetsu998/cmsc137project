package carwars;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import carwars.game.CarWars;
import carwars.init.GameSetup;
import carwars.init.PlayerLogin;
import carwars.util.Config;

public class Main {
	public static void main(String[] args) {
		if(!Config.DEBUG) {
			new PlayerLogin("127.0.0.1", 8080);
		} else {
			try {
				AppGameContainer app = new AppGameContainer(new CarWars("Car Wars"));
				app.setDisplayMode(800, 600, false); //create 800x600 frame
				app.setTargetFrameRate(60); //cap FPS to 60
				app.setAlwaysRender(true);
				app.start();
				
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	public static void launchApp(PlayerLogin pl){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	new GameSetup(pl);
	        }
	    });
	}
}
