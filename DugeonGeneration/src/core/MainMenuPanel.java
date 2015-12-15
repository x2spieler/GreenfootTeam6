package core;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class MainMenuPanel extends BackgroundPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1320537140564920879L;
	private JTextField textField;

	public MainMenuPanel(final ActionListener start, final ActionListener highscore) {
		super(new ImageIcon("src/images/background/Background.jpg").getImage());
		setBounds(200, 200, 1024, 768);
		setLayout(null);
		final BackgroundPanel panel = new BackgroundPanel(image);
		panel.setBounds(0, 0, 1024, 768);

		panel.setForeground(SystemColor.desktop);
		panel.setFont(new Font("Dialog", Font.PLAIN, 5));
		add(panel);

		JButton GameStart = new JButton();
		GameStart.setBounds(293, 328, 423, 89);
		GameStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				GameStart.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Game-Start-white.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				GameStart.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Game-Start.png")));
			}
		});
		GameStart.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/Game-Start.png")));
		GameStart.setBackground(UIManager.getColor("Button.highlight"));
		GameStart.setContentAreaFilled(false);
		GameStart.setOpaque(false);
		GameStart.setBorder(null);
		GameStart.addActionListener(start);
		JButton eingeben = new JButton("");
		eingeben.setBounds(422, 484, 215, 53);
		eingeben.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				eingeben.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/new-seed-white.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				eingeben.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/new-seed.png")));
			}
		});
		eingeben.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/new-seed.png")));
		eingeben.setContentAreaFilled(false);
		eingeben.setOpaque(false);
		eingeben.setBorder(null);
		
		JButton HighScore = new JButton("");
		HighScore.setBounds(342, 414, 348, 70);
		HighScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		HighScore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				HighScore.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/High-Score-white.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				HighScore.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/High-Score.png")));
			}
		});
		HighScore.setIcon(new ImageIcon(DungeonFrame.class.getResource("/images/background/High-Score.png")));
		HighScore.setContentAreaFilled(false);
		HighScore.setOpaque(false);
		HighScore.setBorder(null);
		
		textField = new JTextField();
		textField.setBounds(476, 543, 282, 48);
		textField.setColumns(10);
		textField.setOpaque(false);
		textField.setBorder(null);
		panel.setLayout(null);
		panel.add(HighScore);
		panel.add(eingeben);
		panel.add(GameStart);
		panel.add(textField);


	}

	public JTextField getSeedTF() {
		return textField;
	}
}
