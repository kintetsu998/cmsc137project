package carwars;

import carwars.chat.PlayerLogin;
import carwars.init.GameSetup;

public class Main {
	public static void main(String[] args) {
		new PlayerLogin("localhost", 8080);
	}
	public static void launchApp(String username){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	new GameSetup(username);
	        }
	    });
	}
}
