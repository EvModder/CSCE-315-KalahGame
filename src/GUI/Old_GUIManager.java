package GUI;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class Old_GUIManager extends GUIManager{
	JFrame waitingFrame, menuFrame;
	Old_BoardWindow boardFrame;
	Integer boardMove;
	public static final Image icon = new ImageIcon(
			Old_GUIManager.class.getClass().getResource("/seeds.png")).getImage();
	
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
	
	public boolean getPieRuleWindow(){
		//TODO: Implement a nice looking window to ask "Do you want to do the pie rule?"
		return (JOptionPane.showConfirmDialog(null,
				"Do you want to take the pie rule?",
				"Pie Rule Decision",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}
	
	//------- board stuff ----------------------------------------------------------//
	public void openBoardWindow(int[] board){
		if(boardFrame != null && boardFrame.isDisplayable()){
			boardFrame = new Old_BoardWindow(this, board);
		}
		boardFrame = new Old_BoardWindow(this, board);
	}
		
	public void closeBoardWindow(){
		if(boardFrame != null){
			boardFrame.dispose();
			boardFrame = null;
		}
	}
	
	public void enableButtons(){
		for(int i=0; i<boardFrame.boardSquares.length/2-1; ++i)
			boardFrame.boardSquares[i].setEnabled(boardFrame.boardSquares[i].getSeeds() != 0);
	}
	public void disableButtons(){
		for(int i=0; i<boardFrame.boardSquares.length/2-1; ++i)
			boardFrame.boardSquares[i].setEnabled(false);
	}
	
	public void updateBoardTimer(long time){
		if(boardFrame != null) boardFrame.updateTimer(time);
	}
	
	protected void setMove(int m){boardMove = m;}
	public int getMove(){int temp = boardMove; boardMove = null; return temp;}
	public boolean hasMove(){return boardMove != null;}
	
	public void updateBoardWindow(int[] board){
		if(boardFrame == null) return;
		for(int i=0; i<board.length; ++i) boardFrame.boardSquares[i].setSeeds(board[i]);
		boardFrame.repaint();
	}
	//------------------------------------------------------------------------------//
	
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