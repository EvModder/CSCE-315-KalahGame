package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import Main.Settings;

public class EditSettingsWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	EditSettingsWindow(MenuWindow menuWindow, Settings settings){
		setTitle("Edit Settings");
		setIconImage(GUIManager.icon);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(new Dimension(700, 300));
		
//		Font font = new Font("Consolas", Font.PLAIN, 18);
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
		
		JLabel housesLabel = new JLabel("Houses per Side: "); housesLabel.setFont(font);
		JTextField chooseHouses = new JTextField(settings.getString("holes-per-side"), 3);
		JPanel housesPanel = new JPanel();
		housesPanel.add(housesLabel); housesPanel.add(chooseHouses);
		housesPanel.setOpaque(false);
		JLabel seedsLabel = new JLabel("Seeds per House: "); seedsLabel.setFont(font);
		JTextField chooseSeeds = new JTextField(settings.getString("seeds-per-hole"), 3);
		JPanel seedsPanel = new JPanel();
		seedsPanel.add(seedsLabel); seedsPanel.add(chooseSeeds);
		seedsPanel.setOpaque(false);
		
		JLabel gameTypeLabel = new JLabel("Type of Game: "); gameTypeLabel.setFont(font);
		JComboBox<String> gameTypeBox = new JComboBox<String>(new String[]{"S","R","C"});
		gameTypeBox.setSelectedItem(settings.getString("game-type"));
		JPanel gameTypePanel = new JPanel();
		gameTypePanel.add(gameTypeLabel); gameTypePanel.add(gameTypeBox);
		gameTypePanel.setOpaque(false);
		
		JLabel playerPickerLabel = new JLabel("Player: "); playerPickerLabel.setFont(font);
		JComboBox<String> playerPickerBox = new JComboBox<String>(new String[]{
				"HumanGUI", "HumanConsole", "DumbAI"/*, "DumbJordanAI"*/,
				"RandomAI", "StrategicAI", "BasicMinMaxAI", "BasicMinMaxAI2"
		});
		playerPickerBox.setSelectedItem(settings.getString("AI-name"));
		JPanel playerPickerPanel = new JPanel();
		playerPickerPanel.add(playerPickerLabel); playerPickerPanel.add(playerPickerBox);
		playerPickerPanel.setOpaque(false);
		
		JLabel useGuiLabel = new JLabel("Use GUI: "); useGuiLabel.setFont(font);
		JButton useGuiButton = new JButton(settings.getBoolean("ai-has-GUI") ? "Yes" : "No");
		useGuiButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				if(useGuiButton.getText().equals("No")){
					settings.set("ai-has-GUI", true);
					useGuiButton.setText("Yes");
				}
				else{
					settings.set("ai-has-GUI", false);
					useGuiButton.setText("No");
				}
			}
		});
		JPanel useGuiPanel = new JPanel();
		useGuiPanel.add(useGuiLabel); useGuiPanel.add(useGuiButton);
		useGuiPanel.setOpaque(false);
		
		JLabel timeLimitLabel = new JLabel("Move time-limit: "); timeLimitLabel.setFont(font);
		JTextField timeLimit = new JTextField(settings.getString("time-limit"), 4);
		JPanel timeLimitPanel = new JPanel();
		timeLimitPanel.add(timeLimitLabel); timeLimitPanel.add(timeLimit);
		timeLimitPanel.setOpaque(false);
		
		JLabel startingPlayerLabel = new JLabel("Starting Player: "); startingPlayerLabel.setFont(font);
		JButton startingPlayer = new JButton(settings.getString("starting-player").equals("S") ? "Me" : "Opp");
		startingPlayer.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				if(startingPlayer.getText().equals("Me")){
					settings.set("starting-player", "F");
					startingPlayer.setText("Opp");
				}
				else{
					settings.set("starting-player", "S");
					startingPlayer.setText("Me");
				}
			}
		});
		JPanel startingPlayerPanel = new JPanel();
		startingPlayerPanel.add(startingPlayerLabel); startingPlayerPanel.add(startingPlayer);
		startingPlayerPanel.setOpaque(false);
		
		JLabel useBEGINLabel = new JLabel("Use 'BEGIN': "); useBEGINLabel.setFont(font);
		JButton useBEGIN = new JButton(settings.getBoolean("use-BEGIN") ? "Yes" : "No");
		useBEGIN.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				if(useBEGIN.getText().equals("No")){
					settings.set("use-BEGIN", true);
					useBEGIN.setText("Yes");
				}
				else{
					settings.set("use-BEGIN", false);
					useBEGIN.setText("No");
				}
			}
		});
		JPanel useBEGINPanel = new JPanel();
		useBEGINPanel.add(useBEGINLabel); useBEGINPanel.add(useBEGIN);
		useBEGINPanel.setOpaque(false);
		
		JLabel customBoardLabel = new JLabel("Custom Setup: "); customBoardLabel.setFont(font);
		//TODO: Customize starting board (give the #'num-seeds' text fields)
		JTextField customBoard = new JTextField(settings.getString("custom-board"), 6);
		JPanel customBoardPanel = new JPanel();
		customBoardPanel.add(customBoardLabel); customBoardPanel.add(customBoard);
		customBoardPanel.setOpaque(false);
		
		JLabel pickButtonLabel = new JLabel("Button Image: "); pickButtonLabel.setFont(font);
		JComboBox<String> pickButton = new JComboBox<String>(new String[]{
				"Hexagon", "Square", "Circle", "Awesome"
		});
		pickButton.setSelectedItem(settings.getString("button-image"));
		JPanel pickButtonPanel = new JPanel();
		pickButtonPanel.add(pickButtonLabel); pickButtonPanel.add(pickButton);
		pickButtonPanel.setOpaque(false);
		
		JButton menuButton = new JButton(new ImageIcon(getClass().getResource("/images/return-to-menu.png")));
		menuButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				System.out.println("Return to Menu button pressed");
				dispose();
				if(chooseHouses.getText().matches("^\\d+$")){
					settings.set("holes-per-side", Integer.parseInt(chooseHouses.getText()));
				}
				if(chooseSeeds.getText().matches("^\\d+$")){
					settings.set("seeds-per-hole", Integer.parseInt(chooseSeeds.getText()));
				}
				String timeLimitStr = timeLimit.getText().replace(",", "");
				if(timeLimitStr.matches("^\\d+$")){
					settings.set("time-limit", Integer.parseInt(timeLimitStr));
				}
				String buttonImg = pickButton.getSelectedItem().toString();
				if(getClass().getResource("/images/"+buttonImg+".png") != null){
					settings.set("button-image", buttonImg);
				}
				settings.set("game-type", gameTypeBox.getSelectedItem());
				settings.set("AI-name", playerPickerBox.getSelectedItem());
				settings.updateFile();
				menuWindow.setVisible(true);
			}
		});
		menuButton.setOpaque(false);
		menuButton.setBorderPainted(false);
		menuButton.setContentAreaFilled(false);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(housesPanel);
		topPanel.add(seedsPanel);
		topPanel.add(gameTypePanel);
		topPanel.setOpaque(false);
		
		JPanel midPanel = new JPanel();
		midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
		midPanel.add(playerPickerPanel);
		midPanel.add(useGuiPanel);
		midPanel.add(timeLimitPanel);
		midPanel.setOpaque(false);
		
		JPanel mid2Panel = new JPanel();
		mid2Panel.setLayout(new BoxLayout(mid2Panel, BoxLayout.X_AXIS));
		mid2Panel.add(startingPlayerPanel);
		mid2Panel.add(useBEGINPanel);
		mid2Panel.add(customBoardPanel);
		mid2Panel.setOpaque(false);
		
		JPanel endPanel = new JPanel();
		endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.X_AXIS));
		endPanel.add(pickButtonPanel);
//		endPanel.add();
//		endPanel.add();
		endPanel.setOpaque(false);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(topPanel);
		panel.add(midPanel);
		panel.add(mid2Panel);
		panel.add(endPanel);
		panel.setOpaque(false);
		
		setContentPane(new JComponent(){
			private static final long serialVersionUID = 1L;
			Image woodtexture = new ImageIcon(getClass().getResource("/images/wood2.jpeg")).getImage();
			@Override protected void paintComponent(Graphics g){
				g.drawImage(woodtexture, 0, 0, getWidth(), getHeight(), 0, 0,
						woodtexture.getWidth(null), woodtexture.getHeight(null), null);
				super.paintComponent(g);
			}
		});
		setLayout(new BorderLayout());
		add(menuButton, BorderLayout.NORTH);
		add(panel);
		setMinimumSize(new Dimension(200, 80));
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
