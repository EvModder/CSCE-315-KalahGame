package AI;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import GUI.BoardWindow;
import Main.Board;

public class AIWithGUI extends KalahPlayer {
	BoardWindow boardFrame;
	KalahPlayer ai;
	
	public AIWithGUI(Board board, Class<?> clazz) {
		super(board);
		try{
			ai = (KalahPlayer) clazz.getConstructor(Board.class).newInstance(board);
		}
		catch(InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e){
			System.err.println("Invalid AI-name in settings.yml");
		}
		boardFrame = new BoardWindow(board.housesAndKalahs);
	}

	@Override public List<Integer> getMove() {
		List<Integer> moves = ai.getMove();
		boardFrame.updateBoard(ai.board.housesAndKalahs);
		return moves;
	}
	
	@Override public void updateBoard(Board board) {
		ai.board = board;
		boardFrame.updateBoard(board.housesAndKalahs);
	}
	
	@Override public void closeGame(){
		boardFrame.dispose();
	}
	
	@Override public void applyOpponentMove(int move) {
		ai.applyOpponentMove(move);
		boardFrame.updateBoard(board.housesAndKalahs);
	}
}
