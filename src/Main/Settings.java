package Main;

import java.util.Map;

public class Settings {
	private static Map<String, String> map;
	
	public static String getSetting(String key){
		//load settings
		if(map == null) map = FileIO.loadYaml("settings.yml",
						KalahGame.class.getResourceAsStream("/settings.yml"));
		return map.get(key);
	}
	
	public static void changeSetting(String key, String newValue){
		map.put(key, newValue);
		//TODO: save back to file (I will be changing the format away from YML soon)
	}
}
