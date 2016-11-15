package carwars.model;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Animation;

import carwars.util.Config;

public class Queue extends ArrayList<Player> {
	private static final long serialVersionUID = 1L;

	public Queue(String pNames) {
		String[] tok = pNames.split(" ");
		Random r = new Random();
		
		for(String s : tok) {
			this.add(new Player(s, new Animation(), r.nextInt(Config.MAP_WIDTH), null, null));
		}
	}
	
	public void start() {
		this.get(0).start();
	}

	public void next() {
		Player p = this.remove(0);
		
		p.end();
		this.add(p);
		this.get(0).start();
	}
	
	public String returnStatus() {
		return this.get(0).toString();
	}
	
	public String returnStatuses() {
		String status = " ";
		
		for(Player p : this) {
			status += p.toString() + ",";
		}
		
		return status;
	}
}
