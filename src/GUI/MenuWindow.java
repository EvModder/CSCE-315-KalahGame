package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import Main.KalahGame;
import Main.Settings;

public class MenuWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	Settings settings;

	MenuWindow(GUIManager guiHandler){
		settings = new Settings();
		setTitle("~ Kalah ~");
		setIconImage(GUIManager.icon);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(400, 200));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel label = new JLabel("Welcome to the Main Menu", SwingConstants.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		JButton joinButton = new JButton("Join Game");
		joinButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("Join button pressed");
				settings.set("is-server", false);
				new KalahGame(guiHandler, settings);
			}
		});
		
		JButton hostButton = new JButton("Host Game");
		hostButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("Host button pressed");
				settings.set("is-server", true);
				new KalahGame(guiHandler, settings);
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
		
		JButton editSettingsButton = new JButton("Edit Settings");
		editSettingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(MenuWindow.this.isVisible()){
					MenuWindow.this.setVisible(false);
					System.out.println("Edit Settings button pressed");
					new EditSettingsWindow(MenuWindow.this, settings);
				}
			}
		});
		JButton reloadSettingsButton = new JButton("Reload Settings");
		reloadSettingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Reload Settings button pressed");
				settings = new Settings();
			}
		});
		buttonPanel = new JPanel(new GridLayout());
		buttonPanel.add(editSettingsButton);
		buttonPanel.add(reloadSettingsButton);
		panel.add(buttonPanel);
		
		add(label, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
