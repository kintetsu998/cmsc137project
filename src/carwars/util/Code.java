package carwars.util;

import java.util.ArrayList;

public class Code {
	static public final String START_CODE = "@start";
	public static final String GET_ALL_STATUS = "@status ";
	public static final String UPDATE_STATUS = "@update ";
	public static final String UDP_STOP_STATUS = "@stopUDP";
	
	public static final String PLAYER_LIST = "@list ";
	public static final String PLAYER_NAME = "@name ";
	public static final String PLAYER_JOIN = "@join ";
	
	public static final String CREATE_BULLET = "@bullet ";
	
	public static final String MAP_ID = "@map ";
	
	static private ArrayList<String> codes = new ArrayList<>();
	
	public static void init() {
		codes.add(START_CODE);
		codes.add(GET_ALL_STATUS);
		codes.add(UPDATE_STATUS);
		codes.add(UDP_STOP_STATUS);

		codes.add(PLAYER_LIST);
		codes.add(PLAYER_NAME);
		codes.add(PLAYER_JOIN);
		
		codes.add(CREATE_BULLET);
		codes.add(MAP_ID);
	}
	
	public static boolean codeExists(String code) {
		if(codes.size() == 0) {
			init();
		}
		
		for(String c : codes) {
			if(code.contains(c)) {
				return true;
			}
		}
		
		return false;
	}
}
