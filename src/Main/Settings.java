package Main;

import java.util.Map;

public class Settings {
	//TODO: Completely remodel this class -- the disgusting staticness is horrible
	//and makes impossible to host a server which plays against multiple clients
	//Possible fix to the staticness:
	/*
	 * make a public constructor, also make a static function "getInstance()"
	 * that returns a static instance. That way, the multi-game server can use
	 * the constructor to create as many instances as it needs...
	 */
	private static Map<String, String> map;
	
	public static String getSetting(String key){
		//load settings
		if(map == null) map = FileIO.loadYaml("settings.yml",
						KalahGame.class.getResourceAsStream("/settings.yml"));
		return map.get(key);
	}
	
	public static void changeSetting(String key, String newValue){
		if(map == null) map = FileIO.loadYaml("settings.yml",
				KalahGame.class.getResourceAsStream("/settings.yml"));
		map.put(key, newValue);
		//TODO: save back to file (I will be changing the format away from YML soon)
	}
}
