package AI;
import java.util.ArrayList;
import java.util.List;

import Main.Board;

/*
 * An expansion of DumbAI
 * This AI does not use a MIN-MAX tree, but does cleverly select moves
 * to maximize the seeds it can get on its turn.
 */
public class StrategicAI extends KalahPlayer{
	public StrategicAI(Board board){
		super(board);
	}
	
	static class Pair{
		int idx,value;
		Pair(int a, int b){
			idx=a;
			value=b;
		}
	}
	
	int moveValue = Integer.MIN_VALUE, lastValue;
	
	@Override public void applyOpponentMove(int move){
		board.moveSeeds(move);
	}
	
	@Override public List<Integer> getMove(){
		//Whether this is an outer call or a recursive call
		boolean outerMost = (moveValue == Integer.MIN_VALUE);
		
		//List of moves to return
		List<Integer> moves = new ArrayList<Integer>();
		
		//Look for the best capture on the current board state
		Pair capture = getBestCapture(board);
		int captureValue=-1;
		Board boardIfCapture = null;
		
		//If there is a capture available, figure out what it will do to the boarx
		if(capture.idx != -1){
			boardIfCapture = board.getCopy();
			boardIfCapture.moveSeeds(capture.idx);
			captureValue = calculateUtilityValue(boardIfCapture);
		}
		
		boolean noMove = true;
		for(int i=board.kalah1()-1; i>=0; --i){
			if(board.validMove(i) && board.willHitKalah(i)){
				board.moveSeeds(i);
				
				//Decide whether to do move that hits the Kalah (and all the moves that follow),
				//or to do to the current best capture.
				List<Integer> futureMoves = getMove();
				if(capture.idx == -1 || moveValue >= captureValue){
					moves.add(i);
					moves.addAll(futureMoves);
				}
				else{
					board = boardIfCapture;//reset board
					moves.add(capture.idx);
					board.moveSeeds(capture.idx);
				}
				noMove = false;
				break;
			}
		}
		
		//If unable to find a move that hit the Kalah
		if(noMove){
			//If able to find a capture, do it
			if(capture.idx != -1){
				moves.add(capture.idx);
				board.moveSeeds(capture.idx);
			}
			else{
				int loop = getBestLoop(board);
				if(loop != -1){
					moves.add(loop);
					board.moveSeeds(loop);
				}
			}
		}
		if(outerMost){
			//This is the outermost call, so we need to reset move
			//value estimations for future calls to this AI
			lastValue = moveValue;
			moveValue = Integer.MIN_VALUE;
		}
		else moveValue = Math.max(calculateUtilityValue(board), moveValue);
		return moves;
	}
	
	//Estimated utility value of a board
	private int calculateUtilityValue(Board board){
		if(!board.gameNotOver()) board.collectLeftoverSeeds();
		return board.getScoreDifference() + board.getSeedDifference()/board.kalah2();//kalah2 = length-1
	}
	
	//Current best-known utility value
	int getUtilityValue(){
		return lastValue;
	}
	
	static Pair getBestCapture(Board board){
		int max=0,maxI=-1;
		for(int i=0; i<board.kalah1(); ++i){
			int val = board.captureValue(i);
			if(val > max){
				max = val;
				maxI = i;
			}
/*			if(board.housesAndKalahs[i] == 0 || board.housesAndKalahs[i] > board.kalah2) continue;
			
			int capture = board.housesAndKalahs.length-2-i;
			if(board.housesAndKalahs[i] == board.kalah2){
				if(board.housesAndKalahs[capture]+2 > max){
					maxI = i;//land = i
					max = board.housesAndKalahs[capture]+2;
				}
			}
			else{
				int land = i + board.housesAndKalahs[i];
				int inKalah = 0;
				
				if(land >= board.kalah2){
					land -= board.kalah2;
					++inKalah;
				}
				if(land < board.numHouses && board.housesAndKalahs[land] == 0){
					inKalah += board.housesAndKalahs[capture];	
					if(inKalah > max){
						maxI = i;
						max = inKalah;
					}
				}
			}*/
		}
		return new Pair(maxI, max);
	}
	
	static int getBestLoop(Board board){
		int max=-1,maxI=-1;
		for(int i=0; i<board.kalah1(); ++i){
			if(board.housesAndKalahs[i] == 0) continue;
			int inKalah = (board.housesAndKalahs[i]-1) / (board.kalah2());
			if(i+board.housesAndKalahs[i] >= board.kalah1()) ++inKalah;
			
			if(inKalah > max){
				max = inKalah;
				maxI = i;
			}
		}
		return maxI;
	}
}