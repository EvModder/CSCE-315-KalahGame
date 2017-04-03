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
	private static ImageIcon buttonImg = new ImageIcon(HouseButton.class.getResource("/button.png"));
	private int seeds;
	private FontMetrics fm;

	public HouseButton(GUIManager gui, int i, int initialSeeds){
		seeds = initialSeeds;
		addActionListener(new ButtonListener(gui, i));
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
		if(isEnabled())
			g.drawImage(buttonImg.getImage(), 0, 0, getWidth(), getHeight(), 0, 0,
					buttonImg.getIconWidth(), buttonImg.getIconHeight(), null);
		
		
		String str = String.valueOf(seeds);
		fm.stringWidth(str);
		g.drawString(str, getWidth()/2-fm.stringWidth(str)/2, getHeight()/2+fm.getAscent()/2);
	}
	
	class ButtonListener implements ActionListener{
		GUIManager board;
		int idx;
		ButtonListener(GUIManager board, int i){
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

/*
	@Override public Dimension getPreferredSize() {
		Dimension d = super.getPreferredSize();
		Container c = getParent();
		if(c != null) d = c.getSize();
		else return new Dimension(10, 10);

		int w = (int) d.getWidth();
		int h = (int) d.getHeight();
		int s = (w < h ? w : h);
		return new Dimension(s, s);
	}//*/
	
	@Override
	public void setSeeds(int i) {
//		setText(""+seeds);
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