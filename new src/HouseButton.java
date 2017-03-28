import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class HouseButton extends JButton implements KalahSquare{
	private static final long serialVersionUID = 1L;
	int seeds;

	HouseButton(BoardFrame board, int i, int initialSeeds){
		super(""+initialSeeds);
		seeds = initialSeeds;
		addActionListener(new ButtonListener(board, i));
		setEnabled(false);
	}
	
	class ButtonListener implements ActionListener{
		BoardFrame board;
		int idx;
		ButtonListener(BoardFrame b, int i){
			board = b;
			idx = i;
		}
		@Override public void actionPerformed(ActionEvent evt){
//			System.out.println("Button"+idx+" pressed");
//			board.moveSeeds(idx);
			KalahGame.move = idx;
			KalahGame.waitingForMove = false;
		}
	};
	
	@Override
	public void setSeeds(int i) {
		seeds = i;
		setText(""+i);
	}

	@Override
	public int getSeeds() {
		return seeds;
	}
	
	@Override
	public void addSeeds(int i) {
		setSeeds(i+seeds);
	}
}