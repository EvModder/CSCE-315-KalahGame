package GUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import Main.KalahSquare;
import java.awt.FontMetrics;

public class HouseButton extends JButton implements KalahSquare{
	private static final long serialVersionUID = 1L;
	private static ImageIcon buttonImg = new ImageIcon(HouseButton.class.getResource("/hexagon.png"));
	private int seeds;
	private FontMetrics fm;

	public HouseButton(BoardWindow board, int i, int initialSeeds){
		seeds = initialSeeds;
		addActionListener(new ButtonListener(board, i));
		setEnabled(false);
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setForeground(Color.BLACK);
		setFont(new Font("Consolas", Font.BOLD, 20));
		fm = getFontMetrics(getFont());
	}
	
	@Override protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(buttonImg.getImage(), 0, 0, getWidth(), getHeight(), 0, 0,
				buttonImg.getIconWidth(), buttonImg.getIconHeight(), null);
		if(isEnabled()){
			g.drawImage(buttonImg.getImage(), 0, 0, getWidth(), getHeight(), 0, 0,
					buttonImg.getIconWidth(), buttonImg.getIconHeight(), null);
			g.setColor(Color.LIGHT_GRAY);
		}
		String str = String.valueOf(seeds);
		fm.stringWidth(str);
		g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, getHeight()/2+fm.getAscent()/2);
	}
	
	class ButtonListener implements ActionListener{
		BoardWindow board;
		int idx;
		ButtonListener(BoardWindow board, int i){
			this.board = board;
			idx = i;
		}
		@Override public void actionPerformed(ActionEvent evt){
			synchronized(board){
				if(!board.hasMove()){
					board.setMove(idx);
				}
			}
		}
	};
	
	@Override
	public void setSeeds(int i) {
		seeds = i;
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