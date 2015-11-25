package core;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class MainMenuPanel extends BackgroundPanel {
	private JTextField textField;

	public MainMenuPanel() {
		super(new ImageIcon("src/images/background/background2.jpg").getImage());

		setBackground(SystemColor.text);

		setForeground(SystemColor.desktop);
		setFont(new Font("Dialog", Font.PLAIN, 5));
		setLayout(null);

		Button GameStart = new Button("Game    Start");
		GameStart.setBackground(Color.WHITE);
		GameStart.setBounds(199, 171, 300, 50);
		GameStart.setFont(new Font("Dialog", Font.PLAIN, 31));
		add(GameStart);

		Button Highscore = new Button("High    Score");
		Highscore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		Highscore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);

			}
		});
		Highscore.setActionCommand("");
		Highscore.setFont(new Font("Dialog", Font.PLAIN, 30));
		Highscore.setBackground(UIManager.getColor("Button.highlight"));
		Highscore.setBounds(199, 280, 300, 50);
		add(Highscore);

		textField = new JTextField();
		textField.setBounds(199, 124, 206, 22);
		add(textField);
		textField.setColumns(10);

		Button eingeben = new Button("eingeben");
		eingeben.setFont(new Font("Dialog", Font.BOLD, 16));
		eingeben.setBackground(UIManager.getColor("Button.focus"));
		eingeben.setBounds(410, 123, 121, 22);
		add(eingeben);

	}
}
