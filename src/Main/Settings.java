package Main;
import java.util.Map;

public class Settings {
	private Map<String, String> map;
	private String filename = "settings.yml";
	
	public Settings(String filename){
		this.filename = filename;
		
		//Initialize this Settings object with a map (setting -> value)
		map = FileIO.loadYaml(filename, getClass().getResourceAsStream("/settings.yml"));
	}
	
	public Settings(){
		this("settings.yml");
	}
	
	public boolean getBoolean(String key){
		return Boolean.parseBoolean(map.get(key.toLowerCase()));
	}
	public int getInt(String key){
		String val = map.get(key.toLowerCase());
		return val != null && val.matches("\\d+") ? Integer.parseInt(val) : 0;
	}
	
	public String getString(String key){
		return map.get(key.toLowerCase());
	}
	
	public void set(String key, Object value){
		map.put(key.toLowerCase(), value.toString());
	}
	
	public void updateFile(){
		//Save current settings back to the file their were loaded from
		FileIO.saveYaml(filename, map);
	}
}
