package core;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.UIManager;
public class HighScore extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3351359252247559119L;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HighScore frame = new HighScore();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HighScore() {
		Image image = new ImageIcon("src/images/background/highscore.jpg")
		.getImage();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 692, 550);
		getContentPane().setLayout(new BorderLayout(0, 0));
		BackgroundPanel panel = new BackgroundPanel(image);
		panel.setBackground(SystemColor.text);
		
		panel.setForeground(SystemColor.desktop);
		panel.setFont(new Font("Dialog", Font.PLAIN, 5));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnZruck = new JButton("züruck");
		btnZruck.setForeground(UIManager.getColor("Button.highlight"));
		btnZruck.setFont(new Font("DejaVu Sans", Font.BOLD, 23));
		btnZruck.setBounds(427, 367, 151, 51);
		panel.add(btnZruck);
		btnZruck.setOpaque(false);
		btnZruck.setContentAreaFilled(false);
		btnZruck.setOpaque(false);
		
		JLabel lblYourHighscoreIs = new JLabel("Your HighScore is: ");
		lblYourHighscoreIs.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 38));
		lblYourHighscoreIs.setForeground(SystemColor.text);
		lblYourHighscoreIs.setBounds(67, 82, 418, 51);
		panel.add(lblYourHighscoreIs);
		
	}
}
