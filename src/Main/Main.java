package Main;
import javax.swing.ImageIcon;
import GUI.GUIManager;

class Main{
	public static void main(String... args){
		String osName = System.getProperty("os.name").toLowerCase();
		boolean isMacOs = osName.startsWith("mac os x");
		if(isMacOs){
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Kalah");
			System.setProperty("apple.awt.application.name", "Kalah");
			
			com.apple.eawt.Application.getApplication().setDockIconImage(
					new ImageIcon(Main.class.getResource("/images/seeds.png")).getImage());
		}
		new GUIManager().openMenuWindow();
	}
}