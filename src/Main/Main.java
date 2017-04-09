package Main;
import java.lang.reflect.InvocationTargetException;
import GUI.GUIManager;

class Main{
	public static void main(String... args){
		String osName = System.getProperty("os.name").toLowerCase();
		boolean isMacOs = osName.startsWith("mac os x");
		if(isMacOs){
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Kalah");
			System.setProperty("apple.awt.application.name", "Kalah");
			
			//Reflection... sorry, there is no code style that could make this look good
			try{
				Class<?> clazz = Class.forName("com.apple.eawt.Application");
				Object application = clazz.getMethod("getApplication").invoke(null, new Object[]{});
				clazz.getMethod("setDockIconImage", java.awt.Image.class).invoke(application, 
						new javax.swing.ImageIcon(Main.class.getResource("/images/seeds.png")).getImage());
			}
			catch(IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException | ClassNotFoundException e){
				e.printStackTrace();
			}
			
		}
		new GUIManager().openMenuWindow();
	}
}