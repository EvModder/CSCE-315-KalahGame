package Main;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Utils {
	private static JFrame waitingFrame, menuFrame;
	private static JLabel waitingLabel;
	private static Timer waitingTimer;
	private static Map<String,String> settings;
	
	public static Map<String, String> getSettings(){
		//load settings
		if(settings == null) settings = FileIO.loadYaml("settings.yml",
						KalahGame.class.getResourceAsStream("/settings.yml"));
		return settings;
	}
	
	public interface TimerListener{
		void timerEnded();
	}
	public static void startTimer(TimerListener listener, long timelimit){
		if(waitingTimer != null) waitingTimer.cancel();
		waitingTimer = new Timer();
		waitingTimer.schedule(new TimerTask(){
			@Override public void run(){
				listener.timerEnded();
				if(waitingTimer != null) waitingTimer.cancel();
			}
		}, timelimit);
	}
	public static void cancelTimer(){
		waitingTimer.cancel();
		waitingTimer = null;
	}
	
	public static void openMenuWindow(){
		if(menuFrame != null){
			menuFrame.setVisible(true);
			return;
		}
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

System.out.println("The objective is to have more seeds in your 'Kalah' at the end of the game.\n"
		+ "You and your opponent take turns moving the seeds according to the following rules.\n"
		+ "1. You can only move the seeds on your side of the Kalah board.\n"
		+ "2. The seeds will move in a counter-clockwise direction\n"
		+ "3. To move, select a non-empty house on your side of the board\n"
		+ "4. If the last seed lands on your Kalah, you get to go again.\n"
		+ "5. If the last seed lands on an empty house on your side,"
		+ "you get all the seeds from your opponent's house directly opposite from yours.\n");

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
	public static void closeMenuWindow(){
		if(menuFrame != null) menuFrame.setVisible(false);
	}
	
	public static void openWaitingWindow(){
		if(waitingFrame == null){
			waitingFrame = new JFrame("Waiting to start");
			waitingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			waitingLabel = new JLabel("Waiting for opponent", SwingConstants.CENTER);
			waitingFrame.setSize(300, 100);
			waitingFrame.add(waitingLabel);
			waitingFrame.setLocationRelativeTo(null);
		}
		waitingFrame.setVisible(true);
		
		waitingTimer = new Timer();
		waitingTimer.schedule(new TimerTask(){@Override public void run() {
			waitingLabel.setText((" "+waitingLabel.getText()+'.')
					.replace("....", "").replace("    ", ""));
			waitingFrame.repaint();
		}}, 500, 300);
	}
	public static void closeWaitingWindow(){
		if(waitingFrame == null) return;
		waitingFrame.setVisible(false);
//		waitingFrame.dispose();
		waitingTimer.cancel();
//		waitingFrame = null;
		waitingTimer = null;
	}
	
	public static void connectionErrorWindow(){
		//TODO: Implement a nice looking window to display "Connection Error"
		JOptionPane.showMessageDialog(null, "Error - Unable to connect!", 
											"Connection issue", JOptionPane.ERROR_MESSAGE);
	}
	
	public static String getHostNameWindow(){
		//TODO: Implement a nice looking window to ask "What is the host address?"
		return JOptionPane.showInputDialog("Please enter the Host Address", "localhost");
	}
	
	public static boolean getPieRuleWindow(){
		//TODO: Implement a nice looking window to ask "Do you want to do the pie rule?"
		return (JOptionPane.showConfirmDialog(null,
				"Do you want to take the pie rule?",
				"Pie Rule Decision",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}
	
	public enum GameResult{WON,LOST,TIED,TIME};
	public static void openGameOverWindow(GameResult ending){
		//TODO: Implement a nice looking window displaying the game result
		String message = "Tie?";
		switch(ending){
			case LOST:
				message = "You lose!";
				break;
			case WON:
				message = "You win!";
				break;
			case TIED:
				message = "The game was a tie!";
				break;
			case TIME:
				message = "You did not move in time!";
				break;
		}
		JOptionPane.showMessageDialog(null, message, "Game Over", JOptionPane.DEFAULT_OPTION);
	}
}