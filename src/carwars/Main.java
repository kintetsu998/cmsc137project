package carwars;

import carwars.init.PlayerLogin;
import carwars.util.Config;

public class Main {
	public static void main(String[] args) {		
		new PlayerLogin(Config.TCP_SERVER_IP, Config.TCP_PORT);
	}
}
