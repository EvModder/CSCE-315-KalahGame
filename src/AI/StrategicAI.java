package AI;
import java.util.ArrayList;
import java.util.List;

import Main.Board;

public class StrategicAI extends KalahPlayer{
	public StrategicAI(Board board){super(board);}
	static class Pair{int i,v; Pair(int a, int b){i=a;v=b;}}
	int moveValue;
	
	@Override public void applyOpponentMove(int move){board.moveSeeds(move);}
	@Override public List<Integer> getMove(){
		List<Integer> moves = new ArrayList<Integer>();
		
		Pair capture = getBestCapture(board.housesAndKalahs);
		int captureValue=-1;
		Board boardIfCapture = board.getCopy();
		if(capture.i != -1){
			boardIfCapture.moveSeeds(capture.i);
			captureValue = calculateUtilityValue(boardIfCapture);
		}
		
		boolean noMove = true;
		for(int i=board.numHouses-1; i>=0; --i){
			if(board.validMove(i) && board.willHitKalah(i)){
				board.moveSeeds(i);
				List<Integer> futureMoves = getMove();
				if(capture.i == -1 || moveValue >= captureValue){
					moves.add(i);
					moves.addAll(futureMoves);
				}
				else{
					board = boardIfCapture;//reset board
					moves.add(capture.i);
					board.moveSeeds(capture.i);
				}
				noMove = false;
				break;
			}
		}
		if(noMove){
			if(capture.i != -1){
				moves.add(capture.i);
				board.moveSeeds(capture.i);
			}
			else{
				int loop = getBestLoop(board.housesAndKalahs);
				if(loop != -1){
					moves.add(loop);
					board.moveSeeds(loop);
				}
			}
		}
		moveValue = Math.max(calculateUtilityValue(board), moveValue);
		return moves;
	}
	
	private int calculateUtilityValue(Board board){
		if(!board.gameNotOver()) board.collectLeftoverSeeds();
		return board.getScoreDifference() + board.getSeedDifference()/board.kalah2();//kalah2 = length-1
	}
	int getUtilityValue(){
		return moveValue;
	}
	
	static Pair getBestCapture(int[] board){
		int numHouses = board.length/2-1;
		
		int max=0,maxI=-1;
		for(int i=0; i<numHouses; ++i){
			if(board[i] > board.length-1 || board[i] == 0) continue;
			
			if(board[i] == board.length-1 && board[board.length-2-i]+2 > max){
				maxI = i;
				max = board[board.length-2-i]+2;//land=i
				continue;
			}
			
			int land = i + board[i];
			int inKalah = 0;
			
			if(land >= board.length-1){
				land -= (board.length-1);
				++inKalah;
			}
			if(land < numHouses && board[land] == 0){
				inKalah += board[board.length-2-land];	
				if(inKalah > max){
					maxI = i;
					max = inKalah;
				}
			}
		}
		return new Pair(maxI, max);
	}
	
	static int getBestLoop(int[] board){
		int numHouses = board.length/2-1;
		
		int max=-1,maxI=-1;
		for(int i=0; i<numHouses; ++i){
			if(board[i] == 0) continue;
			int inKalah = (board[i]-1) / (board.length-1);
			if(i+board[i] >= numHouses) ++inKalah;
			
			if(inKalah > max){
				max = inKalah;
				maxI = i;
			}
		}
		return maxI;
	}
}