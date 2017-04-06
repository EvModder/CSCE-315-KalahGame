package AI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Main.Board;

public class HumanConsole extends KalahPlayer{
	int turn;
	boolean waitingForMove;
	Scanner scan = new Scanner(System.in);
	
	public HumanConsole(Board board) {
		super(board);
	}
	
	String getInput(String prompt){
		System.out.print(prompt);
		return scan.nextLine();
	}
	
	void printBoard(){
		StringBuilder builder = new StringBuilder("");
		for(int i=board.kalah2()-1; i>board.kalah1(); --i){
			builder.append(board.housesAndKalahs[i]).append('\t');
		}
		builder.append('\n').append(board.housesAndKalahs[board.kalah2()]);
		for(int i=1; i<board.kalah1(); ++i) builder.append('\t');
		builder.append(board.housesAndKalahs[board.kalah1()]).append('\n');
		for(int i=0; i<board.kalah1(); ++i){
			builder.append(board.housesAndKalahs[i]).append('\t');
		}
		System.out.println(builder.toString());
	}
	
	@Override public List<Integer> getMove() {
		List<Integer> moves = new ArrayList<Integer>();
		waitingForMove = true;
		
		printBoard();

		if(++turn == 2 && getInput("Do Pie Rule? (True/False): ").equalsIgnoreCase("True")){
			board.pieRule();
			moves.add(-1);
			return moves;
		}
		
		boolean anotherMove = true;
		while(anotherMove){
			anotherMove = false;
			
			int move = -1;
			while(move == -1){
				String input = getInput("Enter a move (1-"+board.kalah1()+"): ");
				if(!waitingForMove) break;
				if(!input.matches("^\\d+$") || !board.validMove(move = Integer.parseInt(input)-1)){
					System.out.println("Invalid move!");
					move = -1;
				}
			}
			moves.add(move);
			anotherMove = (board.moveSeeds(move) == board.kalah1() && board.gameNotOver());
			if(anotherMove) printBoard();
		}
		return moves;
	}

	@Override public void applyOpponentMove(int move) {
		++turn;
		board.moveSeeds(move);
	}
	@Override public void closeGame(){
		scan.close();
		board.collectLeftoverSeeds();
		printBoard();
		System.out.println("Game Over!");
	}
	@Override public void updateTimer(long time){
		if(time == 0) waitingForMove = false;
	}
}