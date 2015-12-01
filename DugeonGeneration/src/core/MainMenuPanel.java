package core;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		super(new ImageIcon("src/images/background/background2.jpg").getImage());

		setBackground(SystemColor.text);

		setForeground(SystemColor.desktop);
		setFont(new Font("Dialog", Font.PLAIN, 5));
		setLayout(null);

		JButton GameStart = new JButton();
		GameStart.setIcon(new ImageIcon("src/images/background/StartGame.png"));
		GameStart.addActionListener(start);
		GameStart.setForeground(UIManager.getColor("Button.highlight"));
		GameStart.setBackground(UIManager.getColor("Button.highlight"));
		GameStart.setBounds(178, 149, 344, 90);
		GameStart.setFont(new Font("Dialog", Font.PLAIN, 30));
		add(GameStart);
		GameStart.setContentAreaFilled(false);
		GameStart.setOpaque(false);

		JButton Highscore = new JButton("High  Score");
		Highscore.setForeground(UIManager.getColor("Button.highlight"));
		Highscore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HighScore highscore = new HighScore();
				highscore.setVisible(true);

			}
		});
		Highscore.setFont(new Font("Dialog", Font.PLAIN, 30));
		Highscore.setBackground(UIManager.getColor("Button.highlight"));
		Highscore.setBounds(199, 280, 300, 50);
		add(Highscore);
		Highscore.setContentAreaFilled(false);
		Highscore.setOpaque(false);

		textField = new JTextField();
		textField.setBounds(199, 415, 206, 22);
		add(textField);
		textField.setColumns(10);

		JButton eingeben = new JButton("eingeben");
		eingeben.setForeground(UIManager.getColor("Button.light"));
		eingeben.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		eingeben.setFont(new Font("Dialog", Font.BOLD, 16));
		eingeben.setBackground(UIManager.getColor("Button.focus"));
		eingeben.setBounds(439, 415, 121, 22);
		add(eingeben);
		eingeben.setContentAreaFilled(false);
		eingeben.setOpaque(false);

	}

	public JTextField getSeedTF() {
		return textField;
	}
}
