package Main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FileIO {
	public static final String rootDir = "./";
	
	private static Map<String,String> loadYamlish(File file){
		BufferedReader reader = null;
		try{reader = new BufferedReader(new FileReader(file));}
		catch(FileNotFoundException e){return null;}
		
		HashMap<String,String> map = new HashMap<String,String>();
		if(reader != null){
			try{
				String line;
				
				while((line = reader.readLine()) != null){
					line = line.replace("//", "#").trim();
					int idx = line.indexOf('#');
					if(idx >= 0) line = line.substring(0,idx);
					if(line.contains(":")){
						String[] keyval = line.split(":");
						map.put(keyval[0].trim().toLowerCase(), keyval[1].trim());
					}
				}
				reader.close();
			}catch(IOException e){}
		}
		return map;
	}

	public static Map<String,String> loadYaml(String configName, InputStream defaultConfig){
		if(!configName.endsWith(".yml")){
			System.err.println("Invalid config file!");
			System.err.println("Configuation files must end in .yml");
			return null;
		}
		File file = new File(rootDir+configName);
		if(!file.exists() && defaultConfig != null){
			try{
				//Create Directory
				File dir = new File(rootDir);
				if(!dir.exists())dir.mkdir();
				
				//Create config file from default
				BufferedReader reader = new BufferedReader(new InputStreamReader(defaultConfig));
				
				String line = reader.readLine();
				StringBuilder builder = new StringBuilder(line);
				
				while((line = reader.readLine()) != null){
					builder.append('\n');
					builder.append(line);
				}
				reader.close();
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(builder.toString()); writer.close();
			}
			catch(IOException ex){
				ex.printStackTrace();
				System.err.println("Unable to locate a default config!");
				System.err.println("Could not find /config.yml in plugin's .jar");
			}
			System.out.println("Could not locate configuration file!");
			System.out.println("Generating a new one with default settings.");
		}
		return loadYamlish(file);
	}
	
	public static String loadFile(String filename, String defaultContent) {
		StringBuilder builder = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while((line = reader.readLine()) != null) builder.append(line).append('\n');
			reader.close();
			return builder.substring(0, builder.length()-1);
		}
		catch(IOException e){return defaultContent;}
	}
	
	public static boolean saveFile(String filename, String content) {
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(content); writer.close();
			return true;
		}
		catch(IOException e){return false;}
	}

	public static void saveYaml(String configName, Map<String, String> map) {
		if(!configName.endsWith(".yml")) {
			System.err.println("Invalid config file!");
			System.err.println("Configuation files must end in .yml");
		}
		else{
			Map<String, String> toSave = new HashMap<String, String>(map);
			
			StringBuilder builder = new StringBuilder("");
			for(String line : loadFile(configName, "").split("\n")){
				String beforeComment = line.replace("//", "#");
				if(beforeComment.contains("#")){
					beforeComment = line.substring(0, beforeComment.indexOf("#"));
				}
				if(beforeComment.contains(":")){
					String[] keyval = beforeComment.split(":");
					keyval[0] = keyval[0].trim().toLowerCase();
					
					String newVal = toSave.remove(keyval[0]);
					if(newVal != null) line = line.replace(keyval[1].trim(), newVal);
				}
				builder.append(line).append('\n');
			}
			for(String key : toSave.keySet()){
				builder.append(key).append(": ").append(toSave.get(key)).append('\n');
			}
			saveFile(configName, builder.substring(0,builder.length()-1));
		}
	}
}