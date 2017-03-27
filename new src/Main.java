import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

@SuppressWarnings("unused")
class Main{
	public static void main(String... args){new Main();}
	
	JFrame menu;
	KalahGame game;
	
	Main(){
		JFrame menu = new JFrame("~ Kalah ~");
//		menu.setIconImage(new ImageIcon(Main.class.getResource("/seeds.png")).getImage());
		menu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		menu.setPreferredSize(new Dimension(400, 200));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel label = new JLabel("Welcome to the Main Menu", SwingConstants.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		JButton joinButton = new JButton("Join Game");
		joinButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Join button pressed");
				game = new KalahGame(false, menu);
			}
		});
		
		JButton hostButton = new JButton("Host Game");
		hostButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Host button pressed");
				game = new KalahGame(true, menu);
			}
		});
		
		JPanel buttonPanel = new JPanel(new GridLayout());
		buttonPanel.add(joinButton);
		buttonPanel.add(hostButton);
		panel.add(buttonPanel);
		
		JButton istrButton = new JButton("Instructions");
		istrButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Instructions button pressed");
				//instructions()
				System.out.println("The objective of the game is to have the most seeds in your 'Kalah' by the end of the game. "
						+ "You and your oppenent take turns to move the seeds according to the following rules. \n"
						+ "1. You can only move the seeds on your side of the Kalah board. \n"
						+ "2. The seeds will move in a counter-clockwise direction and be placed in the next house or the player's kalah respectively. \n"
						+ "3. To move, player 1 selects a non-empty house from 0-5, and player 2 selects a non-empty house from 7-12. \n"
						+ "4. If the last seed lands on your Kalah, you get to go again. \n"
						+ "5. If the last seed lands on an empty house on your side, you get all the seeds from your opponent's house that is directly opposite from yours. \n"
						+ "*** To Reset a game type in 'R' and to get the instructions, type in 'I' *** \n");
			}
		});
		buttonPanel = new JPanel(new GridLayout());
		buttonPanel.add(istrButton);
		panel.add(buttonPanel);
		
		menu.add(label, BorderLayout.NORTH);
		menu.add(panel, BorderLayout.CENTER);
		menu.pack();
		menu.setLocationRelativeTo(null);
		menu.setVisible(true);
	}
}