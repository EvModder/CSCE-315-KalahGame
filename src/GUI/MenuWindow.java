package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.MatteBorder;

import Main.KalahGame;
import Main.Settings;

public class MenuWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	Settings settings;

	MenuWindow(final GUIManager guiHandler){
		settings = new Settings();
		setTitle("Ultimate Kalah");
		setIconImage(GUIManager.icon);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension((int) Math.max(screenSize.getWidth()/4, 400),
									   (int) Math.max(screenSize.getHeight()/4, 200)));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 17);
		Color brown = new Color(110, 80, 50);
		
		JLabel label = new JLabel("Welcome to the Main Menu", SwingConstants.CENTER);
		label.setForeground(new Color(220, 215, 210));
		label.setFont(font);
		label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		//Button to join a game
		JButton joinButton = new JButton("Join Game");
		joinButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("Join button pressed");
				settings.set("is-server", false);
				new KalahGame(guiHandler, settings);
			}
		});
		joinButton.setContentAreaFilled(false);
		joinButton.setOpaque(false);
		joinButton.setBorder(new MatteBorder(1, 1, 1, 1, brown));
		joinButton.setFont(font);
		
		//Button to host a game
		JButton hostButton = new JButton("Host Game");
		hostButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("Host button pressed");
				settings.set("is-server", true);
				new KalahGame(guiHandler, settings);
			}
		});
		hostButton.setContentAreaFilled(false);
		hostButton.setOpaque(false);
		hostButton.setBorder(new MatteBorder(1, 1, 1, 1, brown));
		hostButton.setFont(font);
		
		//Add join & host buttons to a panel at the top
		JPanel buttonPanel = new JPanel(new GridLayout());
		buttonPanel.add(joinButton);
		buttonPanel.add(hostButton);
		buttonPanel.setOpaque(false);
		panel.add(buttonPanel);
		
		JButton istrButton = new JButton("Instructions");
		istrButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(MenuWindow.this.isVisible()){
					System.out.println("Instructions button pressed");
					new InstructionsWindow(MenuWindow.this);
					MenuWindow.this.setVisible(false);
				}
			}
		});
		istrButton.setOpaque(false);
		istrButton.setContentAreaFilled(false);
		istrButton.setBorder(new MatteBorder(1, 1, 1, 1, brown));
		istrButton.setFont(font);
		buttonPanel = new JPanel(new GridLayout());
		buttonPanel.add(istrButton);
		buttonPanel.setOpaque(false);
		panel.add(buttonPanel);
		
		//Button that opens EditSettingsWindow
		JButton editSettingsButton = new JButton("Edit Settings");
		editSettingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(MenuWindow.this.isVisible()){
					System.out.println("Edit Settings button pressed");
					new EditSettingsWindow(MenuWindow.this, settings);
					MenuWindow.this.setVisible(false);
				}
			}
		});
		editSettingsButton.setOpaque(false);
		editSettingsButton.setContentAreaFilled(false);
		editSettingsButton.setBorder(new MatteBorder(1, 1, 1, 1, brown));
		editSettingsButton.setFont(font);
		JButton reloadSettingsButton = new JButton("Reload Settings");
		reloadSettingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Reload Settings button pressed");
				settings = new Settings();
			}
		});
		reloadSettingsButton.setOpaque(false);
		reloadSettingsButton.setBorder(new MatteBorder(1, 1, 1, 1, brown));
		reloadSettingsButton.setFont(font);
		
		buttonPanel = new JPanel(new GridLayout());
		buttonPanel.add(editSettingsButton);
//		buttonPanel.add(reloadSettingsButton);//Use this button?
		buttonPanel.setOpaque(false);
		panel.add(buttonPanel);
		panel.setOpaque(false);
		
		//Set a wooden image background for the menu window
		setContentPane(new JComponent(){
			private static final long serialVersionUID = 1L;
			Image woodtexture = new ImageIcon(getClass().getResource("/images/wood3.png")).getImage();
			@Override protected void paintComponent(Graphics g){
				g.drawImage(woodtexture, 0, 0, getWidth(), getHeight(), 0, 0,
						woodtexture.getWidth(null), woodtexture.getHeight(null), null);
				super.paintComponent(g);
			}
		});
		setLayout(new BorderLayout());
		add(label, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
