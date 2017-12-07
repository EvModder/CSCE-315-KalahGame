package GUI;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import Main.ReflectionUtils;

public class MultiClientServerWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	//A basic window that can be used to terminate the MultiClientServerAI
	//This window does nothing except display a string
	JLabel winLabel, loseLabel;
	public MultiClientServerWindow(){
		boolean isMacOS = System.getProperty("os.name").toLowerCase().startsWith("mac os x");
		if(isMacOS){
			ReflectionUtils.setMacDockIcon();
		}
		setTitle("MultiClientServerAI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon(getClass().getResource("/images/seeds.png")).getImage());
		setSize(320, 120);
		setResizable(false);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(40,160,20));
		panel.setOpaque(true);
		
		JLabel label = new JLabel("MultiClientServerAI is running", SwingConstants.CENTER);
		label.setFont(new Font("Consolas", Font.BOLD, 16));
		label.setForeground(new Color(200,255,255));
		label.setOpaque(false);
		panel.add(label);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setOpaque(false);
		winLabel = new JLabel("Games won: 0");
		loseLabel = new JLabel("   Games lost: 0");
		winLabel.setFont(new Font("Consolas", Font.BOLD, 16));
		loseLabel.setFont(new Font("Consolas", Font.BOLD, 16));
		
		winLabel.setOpaque(false);
		loseLabel.setOpaque(false);
		scorePanel.add(winLabel);
		scorePanel.add(loseLabel);
		
		panel.add(scorePanel);
		
		add(panel);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	int wins,losses;
	public void updateScore(boolean won){
		if(won) winLabel.setText("Games won: "+(++wins));
		else loseLabel.setText("   Games lost: "+(++losses));
	}
}
