package GUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import Main.KalahSquare;
import java.awt.FontMetrics;

public class HouseButton extends JButton implements KalahSquare{
	private static final long serialVersionUID = 1L;
	static final double CONV_TO_FONTSIZE = Toolkit.getDefaultToolkit().getScreenResolution() / 72.0;
	private int seeds;
	boolean awesome;
	ImageIcon buttonImg;

	//Create a button using the parent component, the index of the button, and the seeds
	public HouseButton(BoardWindow board, int i, int initialSeeds, ImageIcon img){
		seeds = initialSeeds;
		buttonImg = img;
		addActionListener(new ButtonListener(board, i));
		setEnabled(false);
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setForeground(Color.BLACK);
	}
	
	@Override protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Draw the button image on the screen
		g.drawImage(buttonImg.getImage(), 0, 0, getWidth(), getHeight(), 0, 0,
				buttonImg.getIconWidth(), buttonImg.getIconHeight(), null);
		
		//If the button is enabled, darken the image by drawing it again
		if(isEnabled()){
			g.drawImage(buttonImg.getImage(), 0, 0, getWidth(), getHeight(), 0, 0,
					buttonImg.getIconWidth(), buttonImg.getIconHeight(), null);
			g.setColor(Color.LIGHT_GRAY);
		}
		String str = String.valueOf(seeds);
		
		int fontSize = Math.max(20, (int)((getWidth()/5.0)*CONV_TO_FONTSIZE));
		setFont(new Font("Consolas", Font.BOLD, fontSize));
		
		//Draw the number of seeds on the button with a dynamically sized font
		FontMetrics fm = getFontMetrics(getFont());
		g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, getHeight()/2+fm.getAscent()/2);
	}
	
	//A listener class that takes input moves and sends them to the game board
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