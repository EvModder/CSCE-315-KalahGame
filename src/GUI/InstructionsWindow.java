package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class InstructionsWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	InstructionsWindow(MenuWindow menuWindow){
		setTitle("Kalah Instructions");
		setIconImage(GUIManager.icon);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(new Dimension(700, 300));
		
		JTextArea label = new JTextArea(
				"The objective is to have more seeds in your 'Kalah' at the end of the game.\n"
				+ "You and your opponent take turns moving the seeds according to the following rules:\n\n"
				+ "1. You can only move the seeds on your side of the Kalah board.\n"
				+ "2. The seeds will move in a counter-clockwise direction\n"
				+ "3. To move, select a non-empty house on your side of the board\n"
				+ "4. If the last seed lands on your Kalah, you get to go again.\n"
				+ "5. If the last seed lands on an empty house on your side, "
				+ "you get all the seeds from your opponent's house directly opposite from yours.\n");
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		label.setOpaque(false);
		label.setEditable(false);
		label.setLineWrap(true);
		label.setWrapStyleWord(true);
//		label.setForeground(Color.WHITE);
		
		JButton menuButton = new JButton(new ImageIcon(getClass().getResource("/images/return-to-menu.png")));
		menuButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				System.out.println("Return to Menu button pressed");
				dispose();
				menuWindow.setVisible(true);
			}
		});
		menuButton.setOpaque(false);
		menuButton.setBorderPainted(false);
		
		/*setContentPane(new JComponent(){
			private static final long serialVersionUID = 1L;
			Image woodtexture = new ImageIcon(getClass().getResource("/images/wood3.png")).getImage();
			@Override protected void paintComponent(Graphics g){
				g.drawImage(woodtexture, 0, 0, getWidth(), getHeight(), 0, 0,
						woodtexture.getWidth(null), woodtexture.getHeight(null), null);
				super.paintComponent(g);
			}
		});*/
		setLayout(new BorderLayout());
		add(menuButton, BorderLayout.NORTH);
		add(label);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
