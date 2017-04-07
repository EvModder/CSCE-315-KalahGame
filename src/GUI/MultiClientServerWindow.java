package GUI;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MultiClientServerWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	public MultiClientServerWindow(){
		setTitle("MultiClientServerAI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon(getClass().getResource("/images/seeds.png")).getImage());
		setSize(320, 120);
		setResizable(false);
		JLabel label = new JLabel("MultiClientServerAI is running", SwingConstants.CENTER);
		label.setBackground(new Color(40,160,20));
		label.setForeground(new Color(200,255,255));
		label.setOpaque(true);
		label.setFont(new Font("Consolas", Font.BOLD, 16));
		add(label);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
