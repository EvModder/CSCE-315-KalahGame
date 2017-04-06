package AI;
import java.util.ArrayList;
import java.util.List;
import GUI.BoardWindow;
import Main.Board;

public class HumanGUI extends KalahPlayer{
	BoardWindow boardFrame;
	int turn;
	boolean working, waitingForOpp=true;
	
	public HumanGUI(Board board) {
		super(board);
		boardFrame = new BoardWindow(board.housesAndKalahs);
	}

	@Override public List<Integer> getMove() {
		List<Integer> moves = new ArrayList<Integer>();
		working = true;

		if(++turn == 2 && boardFrame.getPieRuleWindow()){
			board.pieRule();
			moves.add(-1);
			return moves;
		}
		
		boolean anotherMove = true;
		while(anotherMove){
			anotherMove = false;
			boardFrame.enableButtons();
			
			while(!boardFrame.hasMove() && working) Thread.yield();
			if(!working) break;
			int move = boardFrame.getMove();
			
			if(board.validMove(move)){
				moves.add(move);
				if(board.moveSeeds(move) == board.kalah1() && board.gameNotOver()) anotherMove = true;
				boardFrame.updateBoard(board.housesAndKalahs);
			}
			else anotherMove = true;
		}
		boardFrame.disableButtons();
		waitingForOpp = true;
		return moves;
	}

	@Override public void applyOpponentMove(int move) {
		if(waitingForOpp){ ++turn; waitingForOpp = false;}
		board.moveSeeds(move);
		boardFrame.updateBoard(board.housesAndKalahs);
	}
	@Override public void closeGame(){
		boardFrame.dispose();
	}
	@Override public void updateTimer(long time){
		if(time <= 0) working = false;
	}
	@Override public void updateBoard(Board board){
		this.board = board;
		boardFrame.updateBoard(board.housesAndKalahs);
	}
}
