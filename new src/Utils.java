import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;


public class Utils {
	static JFrame waitingFrame, menuFrame;
	static JLabel waitingLabel;
	static Timer waitingTimer;
	
	public static void createMenuWindow(){
		menuFrame = new JFrame("~ Kalah ~");
//		menu.setIconImage(new ImageIcon(Main.class.getResource("/seeds.png")).getImage());
		menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		menuFrame.setPreferredSize(new Dimension(400, 200));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel label = new JLabel("Welcome to the Main Menu", SwingConstants.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		JButton joinButton = new JButton("Join Game");
		joinButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Join button pressed");
				new KalahGame(false);
			}
		});
		
		JButton hostButton = new JButton("Host Game");
		hostButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Host button pressed");
				new KalahGame(true);
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
/*
System.out.println("The objective is to have more seeds in your 'Kalah' at the end of the game.\n"
		+ "You and your opponent take turns moving the seeds according to the following rules.\n"
		+ "1. You can only move the seeds on your side of the Kalah board.\n"
		+ "2. The seeds will move in a counter-clockwise direction\n"
		+ "3. To move, select a non-empty house on your side of the board\n"
		+ "4. If the last seed lands on your Kalah, you get to go again.\n"
		+ "5. If the last seed lands on an empty house on your side,"
		+ "you get all the seeds from your opponent's house directly opposite from yours.\n");
*/
			}
		});
		buttonPanel = new JPanel(new GridLayout());
		buttonPanel.add(istrButton);
		panel.add(buttonPanel);
		
		menuFrame.add(label, BorderLayout.NORTH);
		menuFrame.add(panel, BorderLayout.CENTER);
		menuFrame.pack();
		menuFrame.setLocationRelativeTo(null);
		menuFrame.setVisible(true);
	}
	
	public static void showWaitingWindow(){
		waitingFrame = new JFrame("Waiting to start");
		waitingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		waitingLabel = new JLabel("Waiting for client", SwingConstants.CENTER);
		waitingFrame.setSize(300, 100);
		waitingFrame.add(waitingLabel);
		waitingFrame.setLocationRelativeTo(null);
		waitingFrame.setVisible(true);
		
		waitingTimer = new Timer();
		waitingTimer.schedule(new TimerTask(){@Override public void run() {
			waitingLabel.setText((" "+waitingLabel.getText()+'.')
					.replace("....", "").replace("    ", ""));
			waitingFrame.repaint();
		}}, 500, 300);
	}
	public static void closeWaitingWindow(){
		waitingFrame.setVisible(false);
		waitingFrame.dispose();
		waitingTimer.cancel();
	}
	
	static Map<String,String> settings;
	public static Map<String, String> getSettings(){
		//load settings
		if(settings == null) settings = FileIO.loadYaml("settings.yml",
						KalahGame.class.getResourceAsStream("/settings.yml"));
		
		return settings;
	}
}