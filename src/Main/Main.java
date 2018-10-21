package Main;

import GUI.GUIManager;

class Main{
	public static void main(String... args){
		boolean isMacOS = System.getProperty("os.name").toLowerCase().startsWith("mac os x");
		if(isMacOS){
			ReflectionUtils.setMacDockIcon();
		}
		new GUIManager().openMenuWindow();
	}
}
