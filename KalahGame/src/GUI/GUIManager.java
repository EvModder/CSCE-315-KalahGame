package GUI;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class GUIManager {
	JFrame waitingFrame, menuFrame;
	
	//A standard icon used by all windows in this program
	public static final Image icon = new ImageIcon(
			GUIManager.class.getClass().getResource("/images/seeds.png")).getImage();
	
	public void openMenuWindow(){
		if(menuFrame != null && menuFrame.isDisplayable()){
			menuFrame.setVisible(true);
		}
		else menuFrame = new MenuWindow(this);
	}
	public void closeMenuWindow(){
		if(menuFrame != null) menuFrame.setVisible(false);
	}
	
	public void openWaitingWindow(){
		if(waitingFrame != null && waitingFrame.isDisplayable()){
			waitingFrame.setVisible(true);
		}
		else waitingFrame = new WaitingWindow(this);
	}
	public void closeWaitingWindow(){
		if(waitingFrame != null){
			waitingFrame.dispose();
			waitingFrame = null;
		}
	}
	
	public void connectionErrorWindow(){
		//TODO: Implement a nice looking window to display "Connection Error"
		JOptionPane.showMessageDialog(null, "Error - Unable to connect!", 
											"Connection issue", JOptionPane.ERROR_MESSAGE);
	}
	
	public String getHostWindow(String defaultHost){
		//default host is 'localhost'
		//TODO: Implement a nice looking window to ask "What is the host address?"
		String host = JOptionPane.showInputDialog("Please enter the Host Address", defaultHost);
		return host;
	}
	
	public enum GameResult{WON,LOST,TIED,TIME,ILLEGAL};
	public void openGameOverWindow(GameResult ending){
		//TODO: Implement a nice looking window displaying the game result
		String message = "Tie?";
		switch(ending){
			case LOST:
				message = "You lose!";
				break;
			case WON:
				message = "You win!";
				break;
			case TIED:
				message = "The game was a tie!";
				break;
			case TIME:
				message = "You did not move in time!";
				break;
			case ILLEGAL:
				message = "The server thought you were cheating";
				break;
		}
		JOptionPane.showMessageDialog(null, message, "Game Over", JOptionPane.DEFAULT_OPTION);
	}
	
	public void openGameErrorWindow(String message){
		JOptionPane.showMessageDialog(null, message, "Error In Game", JOptionPane.ERROR_MESSAGE);
	}
}