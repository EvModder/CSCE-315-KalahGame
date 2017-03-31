package Main;
import javax.swing.ImageIcon;

import com.apple.eawt.Application;

class Main{
	public static void main(String... args){
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Kalah");
		System.setProperty("apple.awt.application.name", "Kalah");
		
		Application.getApplication().setDockIconImage(
				new ImageIcon(Main.class.getResource("/seeds.png")).getImage());
		
		Utils.openMenuWindow();
	}
}