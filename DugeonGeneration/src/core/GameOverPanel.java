package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import world.DungeonMap;
import java.awt.event.ActionEvent;

public class GameOverPanel extends BackgroundPanel {
	private static final long serialVersionUID = 1320537140564920879L;
	private JTextField highScoretextField;
	private JLabel loadingLabel;
	private final Random random = new Random();
	private JTextField nameText;

	public GameOverPanel(final ActionListener start, final ActionListener highscore, DungeonMap dm) {
		super(new ImageIcon(GameOverPanel.class.getClassLoader().getResource("images/background/End_Background.png")).getImage());
		setBounds(200, 200, 1024, 768);
		setLayout(null);
		final BackgroundPanel panel = new BackgroundPanel(image);
		panel.setBounds(0, 12, 1024, 768);

		panel.setForeground(SystemColor.desktop);
		panel.setFont(new Font("Dialog", Font.PLAIN, 5));
		add(panel);

		highScoretextField = new JTextField();
		highScoretextField.setBounds(752, 418, 201, 48);
		highScoretextField.setColumns(10);
		highScoretextField.setOpaque(false);
		highScoretextField.setBorder(null);
		panel.setLayout(null);
		panel.add(highScoretextField);
		
		
		JLabel gameover = new JLabel("");
		gameover.setIcon(new ImageIcon(GameOverPanel.class.getResource("/images/background/Gameover.gif")));
		gameover.setBounds(10, 71, 1014, 424);
		panel.add(gameover);
		
		JLabel gotScore = new JLabel("");
		gotScore.setIcon(new ImageIcon(GameOverPanel.class.getResource("/images/background/endPanel/got-score.png")));
		gotScore.setBounds(146, 351, 616, 91);
		panel.add(gotScore);
		
		JLabel name = new JLabel("");
		name.setIcon(new ImageIcon(GameOverPanel.class.getResource("/images/background/endPanel/name.png")));
		name.setBounds(331, 466, 129, 48);
		panel.add(name);
		
		nameText = new JTextField();
		nameText.setBackground(Color.WHITE);
		nameText.setOpaque(false);
		nameText.setBorder(null);
		nameText.setBounds(494, 478, 170, 35);
		panel.add(nameText);
		nameText.setColumns(10);
		
		JButton addHighscores = new JButton("");
		addHighscores.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				addHighscores.setIcon(new ImageIcon(GameOverPanel.class.getResource("/images/background/endPanel/add-to-highscores-shine.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				addHighscores.setIcon(new ImageIcon(GameOverPanel.class.getResource("/images/background/endPanel/add-to-highscores.png")));
			}
		});
		addHighscores.setBackground(UIManager.getColor("Button.highlight"));
		addHighscores.setContentAreaFilled(false);
		addHighscores.setOpaque(false);
		addHighscores.setBorder(null);
		addHighscores.setIcon(new ImageIcon(GameOverPanel.class.getResource("/images/background/endPanel/add-to-highscores.png")));
		addHighscores.setBounds(263, 502, 541, 148);
		panel.add(addHighscores);
		
		JLabel bildText = new JLabel("textBG");
		bildText.setIcon(new ImageIcon(GameOverPanel.class.getResource("/images/background/endPanel/End-text.png")));
		bildText.setBounds(484, 419, 320, 102);
		panel.add(bildText);
		
		JLabel highScorezeigen = new JLabel("19");
		highScorezeigen.setForeground(Color.WHITE);
		highScorezeigen.setFont(new Font("Dialog", Font.BOLD, 55));
		highScorezeigen.setBackground(Color.WHITE);
		highScorezeigen.setBounds(752, 362, 136, 53);
		panel.add(highScorezeigen);

	}
	
	public JTextField getSeedTF() {
		return highScoretextField;
	}
}