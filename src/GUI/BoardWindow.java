package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import Main.Settings;

public class BoardWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	public HouseButton[] boardSquares;
	Integer boardMove;

	public BoardWindow(int[] board){
		JPanel player1Houses = new JPanel(new GridLayout()), player2Houses = new JPanel(new GridLayout());

		boardSquares = new HouseButton[board.length];

		final int kalah1 = board.length/2-1, kalah2 = board.length-1;

		String imgName = new Settings().getString("button-image").toLowerCase();
		ImageIcon player1Img, player2Img;
		if(imgName.equals("awesome")){
			player1Img = new ImageIcon(getClass().getResource("/images/awesome.png"));
			player2Img = new ImageIcon(getClass().getResource("/images/awesome-inverse.png"));
		}
		else{
			player1Img = player2Img = new ImageIcon(getClass().getResource("/images/"+imgName+".png"));
		}

		//add houses
		for(int i=0; i<kalah1; ++i){
			player1Houses.add(boardSquares[i] = new HouseButton(this, i, board[i], player1Img));
		}
		for(int i=kalah2-1; i>kalah1; --i){
			player2Houses.add(boardSquares[i] = new HouseButton(this, i, board[i], player2Img));
		}
		//add kalahs
		boardSquares[kalah1] = new HouseButton(this, kalah1, board[kalah1], player1Img);
		boardSquares[kalah2] = new HouseButton(this, kalah2, board[kalah2], player2Img);
		
		player1Houses.setOpaque(false); player2Houses.setOpaque(false);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(player2Houses);
		panel.add(player1Houses);
		panel.setOpaque(false);
		
//		setTitle("Time: 5:00");//or 'Kalah Board'?
		setContentPane(new JComponent(){
			private static final long serialVersionUID = 1L;
			Image woodtexture = new ImageIcon(getClass().getResource("/images/wood.jpeg")).getImage();
			@Override protected void paintComponent(Graphics g){
				int w = getWidth(), h = getHeight();
				g.drawImage(woodtexture, 0, 0, w, h, 0, 0, 
						woodtexture.getWidth(null), woodtexture.getHeight(null), null);
				
				//Resize the Kalahs if the board gets too big or too small
				//Use a bit shift to only resize when the numbers get too far apart
				if(boardSquares[1].getWidth()>>2 != boardSquares[kalah1].getWidth()>>2){
					Dimension dim = new Dimension(boardSquares[1].getWidth(), boardSquares[kalah1].getHeight());
					boardSquares[kalah1].setPreferredSize(dim);
					boardSquares[kalah2].setPreferredSize(dim);
				}
				super.paintComponent(g);
			}
		});
		setLayout(new BorderLayout());
		setIconImage(GUIManager.icon);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		add(panel);
		boardSquares[kalah1].setPreferredSize(new Dimension(80, 100));
		boardSquares[kalah2].setPreferredSize(new Dimension(80, 100));
		add(boardSquares[kalah1], BorderLayout.EAST);
		add(boardSquares[kalah2], BorderLayout.WEST);
		
		
		//Give buttons the approximate minimum sizes relative to the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int kalahWidth = screenSize.width/Math.max(kalah1,16);
		int minHeight = Math.max((int)(kalahWidth/.64), screenSize.height/8);
		
		//Set the board to the correct size relative to the screen resolution
		setPreferredSize(new Dimension((int)(2.5*kalahWidth+kalah1*kalahWidth),
				Math.max((int)(2.5*kalahWidth), minHeight)));
		setMinimumSize(new Dimension((int)(2.5*kalahWidth+kalahWidth*kalah1/1.64), minHeight));
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void updateTimer(long time){
		setTitle(String.format("Time: %.1f", time/1000F));
	}

	public void updateBoard(int[] board){
		for(int i=0; i<board.length; ++i) boardSquares[i].setSeeds(board[i]);
		repaint();
	}
	
	public void enableButtons(){
		for(int i=0; i<boardSquares.length/2-1; ++i)
			boardSquares[i].setEnabled(boardSquares[i].getSeeds() != 0);
	}
	public void disableButtons(){
		for(int i=0; i<boardSquares.length/2-1; ++i)
			boardSquares[i].setEnabled(false);
	}
	
	protected void setMove(int m){boardMove = m;}
	public int getMove(){int temp = boardMove; boardMove = null; return temp;}
	public boolean hasMove(){return boardMove != null;}
	
	public boolean getPieRuleWindow(){
		//TODO: Implement a nice looking window to ask "Do you want to do the pie rule?"
		return (JOptionPane.showConfirmDialog(null,
				"Do you want to take the pie rule?",
				"Pie Rule Decision",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}
}
