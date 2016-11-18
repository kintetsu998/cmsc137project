package carwars.util;

import java.util.ArrayList;

public class Code {
	static public final String START_CODE = ":start:";
	public static final String GET_ALL_STATUS = ":status: ";
	public static final String UPDATE_STATUS = ":update: ";
	public static final String UDP_STOP_STATUS = ":stopUDP: ";
	
	static private ArrayList<String> codes = new ArrayList<>();
	
	public static void init() {
		codes.add(START_CODE);
		codes.add(GET_ALL_STATUS);
		codes.add(UPDATE_STATUS);
		codes.add(UDP_STOP_STATUS);
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
