package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Old_BoardWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	Old_HouseButton[] boardSquares;

	Old_BoardWindow(Old_GUIManager guiHandler, int[] board){
		JPanel player1Houses = new JPanel(new GridLayout());
		JPanel player2Houses = new JPanel(new GridLayout());
		
		boardSquares = new Old_HouseButton[board.length];
		
		int kalah1 = board.length/2-1, kalah2 = board.length-1;
		
		//add houses
		for(int i=0; i<kalah1; ++i){
			player1Houses.add(boardSquares[i] = new Old_HouseButton(guiHandler, i, board[i]));
		}
		for(int i=kalah2-1; i>kalah1; --i){
			player2Houses.add(boardSquares[i] = new Old_HouseButton(guiHandler, i, board[i]));
		}
		//add kalahs
		boardSquares[kalah1] = new Old_HouseButton(guiHandler, kalah1, board[kalah1]);
		boardSquares[kalah2] = new Old_HouseButton(guiHandler, kalah2, board[kalah2]);
		
		player1Houses.setOpaque(false); player2Houses.setOpaque(false);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(player2Houses);
		panel.add(player1Houses);
		panel.setOpaque(false);
		
		setTitle("Time: 5:00");//or 'Kalah Board'?
		setContentPane(new JComponent(){
			private static final long serialVersionUID = 1L;
			Image woodtexture = new ImageIcon(getClass().getResource("/wood.jpeg")).getImage();
			@Override protected void paintComponent(Graphics g){
				g.drawImage(woodtexture, 0, 0, getWidth(), getHeight(), 0, 0,
						woodtexture.getWidth(null), woodtexture.getHeight(null), null);
				super.paintComponent(g);
			}
		});
		setLayout(new BorderLayout());
		setIconImage(GUIManager.icon);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		add(panel);
//		add(new JLabel("Timer: 5:00", SwingConstants.CENTER), BorderLayout.SOUTH);
		boardSquares[kalah1].setPreferredSize(new Dimension(80, 100));
		boardSquares[kalah2].setPreferredSize(new Dimension(80, 100));
		add(boardSquares[kalah1], BorderLayout.EAST);
		add(boardSquares[kalah2], BorderLayout.WEST);
		setPreferredSize(new Dimension(160+64*kalah1, 160));
		setMinimumSize(new Dimension(160+39*kalah1, 100));
		pack();
		setLocationRelativeTo(guiHandler.menuFrame);
		setVisible(true);
	}

	public void updateTimer(long time){
		setTitle(String.format("%.1f", time/1000F));
	}
	
		
/*	public void betaOpenBoardWindow(int[] board){
		JPanel player1Houses = new JPanel(new GridLayout());
		JPanel player2Houses = new JPanel(new GridLayout());
		
		boardSquares = new HouseButton[board.length];
		
		int kalah1 = board.length/2-1, kalah2 = board.length-1;
		
		//add houses
		for(int i=0; i<kalah1; ++i){
			player1Houses.add(boardSquares[i] = new HouseButton(this, i, board[i]));
		}
		for(int i=kalah2-1; i>kalah1; --i){
			player2Houses.add(boardSquares[i] = new HouseButton(this, i, board[i]));
		}
		//add kalahs
		boardSquares[kalah1] = new HouseButton(this, kalah1, board[kalah1]);
		boardSquares[kalah2] = new HouseButton(this, kalah2, board[kalah2]);
		
		ImageIcon woodtexture = new ImageIcon(getClass().getResource("/wood.jpeg"));
		player1Houses.setOpaque(false); player2Houses.setOpaque(false);
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(woodtexture.getImage(), 0, 0, getWidth(), getHeight(), 0, 0,
						woodtexture.getIconWidth(), woodtexture.getIconHeight(), null);
			}
		};
//		JPanel housePanel = new JPanel(){
//			@Override
//		    public Dimension getPreferredSize() {
//		        Container c = getParent();
//		        if(c != null) return c.getSize();
//				else return new Dimension(10, 10);
//		    }
//		};
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(player2Houses);
		panel.add(player1Houses);
//		housePanel.setOpaque(false);
//		panel.setLayout(new GridBagLayout());
//		panel.add(boardSquares[kalah1]);
//		panel.add(housePanel);
//		panel.add(boardSquares[kalah2]);
		
		setTitle("Time: 5:00");//or 'Kalah Board'?
		setIconImage(icon);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 200));
		setMinimumSize(new Dimension(150+20*kalah1, 100));
		add(panel);
		add(boardSquares[kalah1], BorderLayout.EAST);
		add(boardSquares[kalah2], BorderLayout.WEST);
		pack();
		setLocationRelativeTo(menuFrame);
		setVisible(true);
	}//*/
}
