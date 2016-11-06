package carwars;

import carwars.init.PlayerLogin;
import carwars.init.GameSetup;

public class Main {
	public static void main(String[] args) {
		new PlayerLogin("127.0.0.1", 8080);
	}

	public static void launchApp(PlayerLogin pl){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	new GameSetup(pl);
	        }
	    });
	}
}
