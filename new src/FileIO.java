/** MIT License

Copyright (c) 2017 Nathaniel Leake

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED
 */

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
	
	public static String loadFile(String filename, InputStream defaultValue) {
		if(!filename.contains(rootDir)) filename = rootDir+filename;
		BufferedReader reader = null;
		try{reader = new BufferedReader(new FileReader(filename));}
		catch(FileNotFoundException e){
			if(defaultValue == null) return null;
			
			//Create Directory
			File dir = new File(rootDir);
			if(!dir.exists())dir.mkdir();
			
			//Create the file
			File conf = new File(rootDir+filename);
			StringBuilder builder = new StringBuilder();
			String content = null;
			try{
				conf.createNewFile();
				reader = new BufferedReader(new InputStreamReader(defaultValue));
				
				String line = reader.readLine();
				builder.append(line);
				while(line != null){
					builder.append('\n');
					builder.append(line);
					line = reader.readLine();
				}
				reader.close();
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(conf));
				writer.write(content = builder.toString()); writer.close();
			}
			catch(IOException e1){e1.printStackTrace();}
			return content;
		}
		StringBuilder file = new StringBuilder();
		if(reader != null){
			try{
				String line = reader.readLine();
				
				while(line != null){
					line = line.replace("//", "#").trim();
					if(!line.startsWith("#")){
						file.append(line.split("#")[0].trim());
						file.append('\n');
					}
					line = reader.readLine();
				}
				reader.close();
			}catch(IOException e){}
		}
		if(file.length() > 0) file.substring(0, file.length()-1);
		return file.toString();
	}
	
	public static String loadFile(String filename, String defaultContent) {
		if(!filename.contains(rootDir)) filename = rootDir+filename;
		BufferedReader reader = null;
		try{reader = new BufferedReader(new FileReader(filename));}
		catch(FileNotFoundException e){
			if(defaultContent == null || defaultContent.isEmpty()) return defaultContent;
			
			//Create Directory
			File dir = new File(rootDir);
			if(!dir.exists())dir.mkdir();
			
			//Create the file
			File conf = new File(rootDir+filename);
			try{
				conf.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(conf));
				writer.write(defaultContent);
				writer.close();
			}
			catch(IOException e1){e1.printStackTrace();}
			return defaultContent;
		}
		StringBuilder file = new StringBuilder();
		if(reader != null){
			try{
				String line = reader.readLine();
				
				while(line != null){
					line = line.replace("//", "#").trim();
					if(!line.startsWith("#")){
						file.append(line.split("#")[0].trim());
						file.append('\n');
					}
					line = reader.readLine();
				}
				reader.close();
			}catch(IOException e){}
		}
		if(file.length() > 0) file.substring(0, file.length()-1);
		return file.toString();
	}
	
	public static boolean saveFile(String filename, String content) {
		if(!filename.contains("./plugins")) filename = rootDir+filename;
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(content); writer.close();
			return true;
		}
		catch(IOException e){return false;}
	}
	
	public static String loadResource(Object pl, String filename){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(pl.getClass().getResourceAsStream("/"+filename)));
		
			StringBuilder file = new StringBuilder();
			String line = reader.readLine();
			while(line != null){
				line = line.replace("//", "#").trim();
				if(!line.startsWith("#")) file.append(line.split("#")[0].trim()).append('\n');
				line = reader.readLine();
			}
			reader.close();
			return file.toString();
		}
		catch(IOException ex){ex.printStackTrace();}
		return "";
	}
	
	public static Map<String,String> loadYamlish(File file){
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
	
	public static Map<String,String> loadYaml(String filename, String defaultContent){
		if(!filename.contains("./plugins")) filename = rootDir+filename;
		Map<String,String> yaml = loadYamlish(new File(filename));
		if(yaml == null){
			if(defaultContent == null || defaultContent.isEmpty()) return null;
			
			//Create Directory and file
			File dir = new File(rootDir);
			if(!dir.exists()) dir.mkdir();
			File file = new File(rootDir+filename);
			try{
				file.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(defaultContent);
				writer.close();
			}
			catch(IOException e){e.printStackTrace();}
			return loadYamlish(file);
		}
		return yaml;
	}
}