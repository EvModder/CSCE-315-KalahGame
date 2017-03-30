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
				String line = reader.readLine();
				
				while(line != null){
					line = line.replace("//", "#").trim();
					int idx = line.indexOf('#');
					if(idx > 0) line = line.substring(0,idx);
					if(line.contains(":")){
						String[] keyval = line.split(":");
						map.put(keyval[0].trim(), keyval[1].trim());
					}
					line = reader.readLine();
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
}