package carwars.util;

import java.util.ArrayList;

public class Codes {
	static public final String START_CODE = ":start:";
	static public final String STATUS_CODE = ":status:";
	public static final String GET_ALL_STATUSES = ":statuses";
	
	static private ArrayList<String> codes = new ArrayList<>();
	
	public static void init() {
		codes.add(START_CODE);
		codes.add(STATUS_CODE);
		codes.add(GET_ALL_STATUSES);
	}
	
	public static boolean codeExists(String code) {
		if(codes.size() == 0) {
			init();
		}
		
		for(String c : codes) {
			if(c.equals(code)) {
				return true;
			}
		}
		
		return false;
	}
}
