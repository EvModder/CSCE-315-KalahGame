package AI;

import java.util.ArrayList;
import java.util.List;
import Main.Board;

public class DumbJordanAI extends AI{
	public DumbJordanAI(Board board, int time){super(board, time);}

	boolean second  = false;

	class MODEL {
		public int size() {
			return board.numHouses;
		}
		public int getBin(boolean thing, int pos) {
			return board.housesAndKalahs[pos + (thing ? size()+1 : 0)];
		}
	}

	private final MODEL model = new MODEL();

	private int endPoint(int i) {
		return model.getBin(second, i)%(model.size()*2 + 1) + i;
	}
	protected int decideMove() {
		for(int i = model.size() - 1; i >= 0; --i)
			if(endPoint(i) == model.size())
				return i;
		int capPnt = -1;
		int maxCap = 0;
		for(int i = model.size() - 1; i >= 0; --i)
			if(model.getBin(second, i) > 0 && endPoint(i) < model.size()
					&& model.getBin(false, endPoint(i)) == 0
					&& model.getBin(second, i) < 2*(model.size() + 1)
					&& model.getBin(!second, model.size() - endPoint(i) - 1) > maxCap) {
				capPnt = i;
				maxCap = model.getBin(!second, model.size() - endPoint(i) - 1);
			}
		if(capPnt != -1) return capPnt;
		int maxPnt = model.size() - 1;
		int max = 0;
		for(int i = model.size() - 1; i >= 0; --i)
			if(model.getBin(second, i) > max) {
				maxPnt = i;
				max = model.getBin(second, i);
			}
		return maxPnt;
	}
	@Override public List<Integer> getMove() {
		List<Integer> list = new ArrayList<Integer>();

		int move = decideMove();
		list.add(move);
		
		while(board.willHitKalah(move)){
			board.moveSeeds(move);
			
			move = decideMove();
			if(board.validMove(move)) list.add(move);
			else break;
		}
		return list;
	}
	public boolean doPieRule(Board board, int timelimit) {
		return true;
	}
	@Override public void applyMove(int move){board.moveSeeds(move);}
}
