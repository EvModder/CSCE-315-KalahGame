package AI;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import GUI.BoardWindow;
import Main.Board;

/*
 * A wrapper class that takes another AI class and uses it
 * to get moves while projecting the board to a GUI menu.
 */
public class AIWithGUI extends KalahPlayer {
	BoardWindow boardFrame;
	KalahPlayer ai;
	
	public AIWithGUI(Board board, Class<?> clazz) {
		super(board);
		
		//Reflection to get an AI class object given a AI class name as a string
		try{
			ai = (KalahPlayer) clazz.getConstructor(Board.class).newInstance(board);
		}
		catch(InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e)
		{
			System.err.println("Invalid AI-name in settings.yml");
		}
		boardFrame = new BoardWindow(board.housesAndKalahs);
	}

	@Override public List<Integer> getMove() {
		//Get moves from AI object and display them on the board
		List<Integer> moves = ai.getMove();
		boardFrame.updateBoard(ai.board.housesAndKalahs);
		return moves;
	}
	
	@Override public void updateBoard(Board board) {
		ai.board = board;
		boardFrame.updateBoard(board.housesAndKalahs);
	}
	
	@Override public void updateTimer(long time){
		ai.updateTimer(time);
		boardFrame.updateTimer(time);
	}
	
	@Override public void closeGame(){
		ai.closeGame();
		boardFrame.dispose();
	}
	
	@Override public void applyOpponentMove(int move) {
		ai.applyOpponentMove(move);
		boardFrame.updateBoard(board.housesAndKalahs);
	}
}
