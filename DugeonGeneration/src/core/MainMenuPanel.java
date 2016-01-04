package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import world.DungeonMap;

public class MainMenuPanel extends BackgroundPanel {
	private static final long serialVersionUID = 1320537140564920879L;
	private JTextField textField;
	private JLabel loadingLabel;
	private final Random random = new Random();

	public MainMenuPanel(final ActionListener start, final ActionListener highscore, DungeonMap dm) {
		super(new ImageIcon(MainMenuPanel.class.getClassLoader().getResource("images/background/Background.jpg")).getImage());
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
				GameStart.setIcon(new ImageIcon(MainMenuPanel.class.getResource("/images/background/Game-Start-white.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				GameStart.setIcon(new ImageIcon(MainMenuPanel.class.getResource("/images/background/Game-Start.png")));
			}
		});
		GameStart.setIcon(new ImageIcon(MainMenuPanel.class.getResource("/images/background/Game-Start.png")));
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
				eingeben.setIcon(new ImageIcon(MainMenuPanel.class.getResource("/images/background/new-seed-white.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				eingeben.setIcon(new ImageIcon(MainMenuPanel.class.getResource("/images/background/new-seed.png")));
			}
		});
		
		ImageIcon spinner=new ImageIcon(MainMenuPanel.class.getResource("/images/hud/ajax-loader.gif"));
		loadingLabel=new JLabel(spinner);
		loadingLabel.setText("  LOADING");
		loadingLabel.setForeground(Color.black);
		loadingLabel.setBounds(0, 250, DungeonMap.VIEWPORT_WIDTH, 100);
		loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loadingLabel.setVisible(false);
		panel.add(loadingLabel);
		
		eingeben.setIcon(new ImageIcon(MainMenuPanel.class.getResource("/images/background/new-seed.png")));
		eingeben.setContentAreaFilled(false);
		eingeben.setOpaque(false);
		eingeben.setBorder(null);
		eingeben.addActionListener((a) -> {
			if(!dm.isStartingGame()) 
				textField.setText(String.valueOf(random.nextInt()));
		});
		JButton HighScore = new JButton("");
		HighScore.setBounds(342, 414, 348, 70);
		HighScore.addActionListener(highscore);
		HighScore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				HighScore.setIcon(new ImageIcon(MainMenuPanel.class.getResource("/images/background/High-Score-white.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				HighScore.setIcon(new ImageIcon(MainMenuPanel.class.getResource("/images/background/High-Score.png")));
			}
		});
		HighScore.setIcon(new ImageIcon(MainMenuPanel.class.getResource("/images/background/High-Score.png")));
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
	
	public void setLoadingVisibility(boolean visible)
	{
		loadingLabel.setVisible(visible);
	}

	public JTextField getSeedTF() {
		return textField;
	}
}
