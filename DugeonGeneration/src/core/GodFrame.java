package core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.WorldVisitor;
import menu.BuyItem;
import menu.ShopEntry;
import player.BuffType;
import weapons.abstracts.LongRangeWeapon;
import weapons.abstracts.Weapon;
import world.DungeonMap;

public class GodFrame {

	private JFrame frame;
	private JScrollPane viewPortPane; // ViewportPane is the component the viewport is drawn on - who would have guessed that?
	private JScrollPane pauseMenuPane = null;
	private JScrollPane mainMenuPane = null;
	private JScrollPane gameOverPane = null;
	private JScrollPane nextRoundPane = null;
	private JScrollPane highscorePane = null;
	private HighScorePanel highScorePanel = null;
	private JPanel viewportPanel = null;
	private JLabel seedLabel = null;
	private JLabel coinLabel = null;
	private JLabel buyFeedbackLabel = null;
	private JLabel timeLabel = null;
	private JLabel mediPackLabel = null;
	private JLabel weaponLabel = null;
	private JLabel ammoLabel = null;
	private JLabel healthBarLabel = null;
	private JLabel scoreLabel = null;
	private JLabel noDamageLabel = null;
	private JLabel inTimeLabel = null;
	private JLabel hsScoreInfo = null;
	private BufferedImage healthBarImage = null;
	private DungeonMap world;
	private MainMenuPanel mainMenuPanel = null;

	private LinkedHashMap<String, BuffPanel> buffPanels;
	private final int LABEL_HEIGHT = 80;
	private final int SPACE_BETWEEN_LABELS = 40;
	private final int LABEL_X_START = 10;
	private final int LABEL_Y_START = 225;
	private JTextField seedTF;
	
	private boolean paused=false;

	public GodFrame(JFrame frame, DungeonMap world) {
		this.frame = frame;
		this.world = world;
		buffPanels = new LinkedHashMap<String, BuffPanel>();

		frame.getContentPane().remove(1); // Removes the greenfoot buttons
		viewPortPane = (JScrollPane) frame.getContentPane().getComponent(0); // Component 0 is the JScrollPane containing the viewport
		buildMainMenuGui();
		buildMenuGui();
		buildHUD();
		buildGameOverGui();
		buildNextRoundGui();
		buildHighscoreGui();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.pack();
		frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2, screenSize.height / 2 - frame.getHeight() / 2);
		frame.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK),
				"activate_testing_mode01");
		frame.getRootPane().getActionMap().put("activate_testing_mode01", new TestingModeActivator());
		frame.setTitle("Collosseum of Death (working title)");
		frame.setIconImage(new ImageIcon(MainMenuPanel.class.getResource("/images/taskbarLogo.png")).getImage());
	}

	public void addScrollListener(MouseWheelListener listener) {
		viewPortPane.addMouseWheelListener(listener);
	}

	public void changeToFrame(FrameType type) {
		switch (type) {
		case VIEWPORT:
			changeTo(viewPortPane);
			if(paused)
			{
				paused=false;
				world.setLastTicks();
			}
			Greenfoot.start();
			break;
		case MAIN_MENU:
			changeTo(mainMenuPane);
			Greenfoot.stop();
			break;
		case PAUSE_MENU:
			changeTo(pauseMenuPane);
			Greenfoot.stop();
			paused=true;
			break;
		case GAME_OVER:
			changeTo(gameOverPane);
			hsScoreInfo.setText(String.valueOf(world.getPlayerScore()));
			Greenfoot.stop();
			break;
		case NEXT_ROUND:
			changeTo(nextRoundPane);
			buyFeedbackLabel.setText("");
			Greenfoot.stop();
			break;
		case HIGHSCORE:
			highScorePanel.updateList();
			changeTo(highscorePane);
			break;
		}
		frame.pack();
		frame.revalidate();
		frame.repaint();
	}

	private void changeTo(Component t) {
		frame.getContentPane().remove(viewPortPane);
		frame.getContentPane().remove(mainMenuPane);
		frame.getContentPane().remove(pauseMenuPane);
		frame.getContentPane().remove(gameOverPane);
		frame.getContentPane().remove(nextRoundPane);
		frame.getContentPane().remove(highscorePane);
		frame.getContentPane().add(t);
	}

	public void setLoadingVisibility(boolean visible) {
		mainMenuPanel.setLoadingVisibility(visible);
	}

	public void buildHighscoreGui() {
		highScorePanel = new HighScorePanel(world, 10, (ActionEvent e) -> {
			changeToFrame(FrameType.MAIN_MENU);
		});
		highScorePanel.setLayout(null);
		highScorePanel.setPreferredSize(getPrefSize(highScorePanel));
		highScorePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JScrollPane outer = new JScrollPane(highScorePanel);
		outer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		highscorePane = outer;
	}

	public void buildMenuGui() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(getPrefSize(panel));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Dimension panelSize = panel.getPreferredSize();
		// /////CODE FOR GAME OVER MENU GOES HERE

		JLabel pauseLabel = new JLabel("Game paused");
		pauseLabel.setBounds(0, 100, panelSize.width, 50);
		pauseLabel.setFont(new Font("", Font.PLAIN, 26));
		pauseLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(pauseLabel);

		JButton resume = new JButton();
		int buttonWidth = 150;
		int buttonHeight = 35;
		resume.setBounds(panelSize.width / 2 - buttonWidth / 2, panelSize.height / 2, buttonWidth, buttonHeight);
		resume.setText("Resume");
		resume.addActionListener((ActionEvent e) -> {
			changeToFrame(FrameType.VIEWPORT);
		});
		panel.add(resume);
		// /////

		JScrollPane outer = new JScrollPane(panel);
		outer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		pauseMenuPane = outer;

	}

	public void buildNextRoundGui() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(getPrefSize(panel));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Dimension panelSize = panel.getPreferredSize();
		// /////CODE FOR MENU GOES HERE

		JLabel wonLabel = new JLabel("You won the round!");
		wonLabel.setBounds(0, 25, panelSize.width, 50);
		wonLabel.setFont(new Font("", Font.ITALIC, 24));
		wonLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(wonLabel);

		JPanel entryPanel = new JPanel();
		entryPanel.setLayout(new GridLayout(0, 1));
		int horizontalSpace = 20;
		//TODO find a smart way to get the Icon from the weapon it belongs to
		entryPanel.add(
				new ShopEntry(BuyItem.WEAPON_AXE, new GreenfootImage("enemies/weapons/axe/axe0.png"), 300, 1, world));
		entryPanel.add(new ShopEntry(BuyItem.WEAPON_HAMMER, new GreenfootImage("enemies/weapons/hammer/hammer0.png"),
				250, 1, world));
		entryPanel.add(new ShopEntry(BuyItem.WEAPON_SPEAR, new GreenfootImage("enemies/weapons/spear/spear0.png"), 30,
				250, world));
		entryPanel.add(new ShopEntry(BuyItem.WEAPON_CROSSBOW,
				new GreenfootImage("enemies/weapons/crossbow/crossbow0.png"), 250, 1, world));
		entryPanel.add(new ShopEntry(BuyItem.WEAPON_NINJA_STAR,
				new GreenfootImage("enemies/weapons/ninja_star/ninja_star0.png"), 250, 1, world));
		entryPanel.add(new ShopEntry(BuyItem.BULLET_CROSSBOW_ARROW,
				new GreenfootImage("enemies/bullets/crossbow_arrow.png"), 50, 30, world));
		entryPanel.add(new ShopEntry(BuyItem.BULLET_NINJA_STAR, new GreenfootImage("enemies/bullets/ninja_star.png"),
				50, 30, world));
		entryPanel.add(new ShopEntry(BuyItem.MEDI_PACK, new GreenfootImage("medi_pack.png"), 20, 1, world));

		JScrollPane scrollPane = new JScrollPane(entryPanel);
		int scrollY = 100;
		int scrollHeight = 520;
		scrollPane.setBounds(
				new Rectangle(horizontalSpace, scrollY, panelSize.width - 2 * horizontalSpace, scrollHeight));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane);

		coinLabel = new JLabel();
		updateCoinLabelInShop();
		coinLabel.setBounds(horizontalSpace + 10, scrollY + scrollHeight + 10, 150, 40);
		panel.add(coinLabel);

		noDamageLabel = new JLabel();
		noDamageLabel.setBounds(horizontalSpace + 10, scrollY + scrollHeight + 50, 500, 40);
		noDamageLabel.setFont(new Font("", Font.ITALIC, 14));
		noDamageLabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(noDamageLabel);

		inTimeLabel = new JLabel();
		inTimeLabel.setBounds(horizontalSpace + 10, scrollY + scrollHeight + 70, 500, 40);
		inTimeLabel.setFont(new Font("", Font.ITALIC, 14));
		inTimeLabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(inTimeLabel);

		buyFeedbackLabel = new JLabel();
		buyFeedbackLabel.setBounds(0, scrollY + scrollHeight + 10, panelSize.width, 40);
		buyFeedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(buyFeedbackLabel);

		JButton resume = new JButton();
		int buttonWidth = 150;
		int buttonHeight = 40;
		resume.setBounds(panelSize.width - 2 * buttonWidth, panelSize.height - buttonHeight - 50, buttonWidth,
				buttonHeight);
		resume.setText("Next round");
		resume.addActionListener((ActionEvent e) -> {
			world.startNewRound();
			changeToFrame(FrameType.VIEWPORT);
		});
		panel.add(resume);

		// /////

		JScrollPane outer = new JScrollPane(panel);
		outer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		nextRoundPane = outer;
	}

	public void updateCoinLabelInShop() {
		if (world != null && world.getPlayer() != null)
			coinLabel.setText("Coins: " + world.getPlayer().getScore());
	}

	public void setNoDamageLabelText(String s) {
		noDamageLabel.setText(s);
	}

	public void setInTimeLabelText(String txt) {
		inTimeLabel.setText(txt);
	}

	public void updateFeedbackLabel(boolean success, String msg) {
		if (success)
			buyFeedbackLabel.setForeground(new Color(0, 150, 0));
		else
			buyFeedbackLabel.setForeground(new Color(184, 0, 0));
		buyFeedbackLabel.setText(msg);
	}

	public void setNewSeedForTextField() {
		seedTF.setText("" + new Random().nextInt());
	}

	public void buildMainMenuGui() {
		mainMenuPanel = new MainMenuPanel((ActionEvent e) -> {
			//Start-Button
			if (world.isStartingGame())
				return;
			try {
				String s = seedTF.getText();
				int seed = !s.equals("") ? Integer.parseInt(s) : new Random().nextInt();
				world.startNewGame(seed);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} ,
				//Highscore-Button
				(ActionEvent e) -> {
					if (world.isStartingGame())
						return;
					changeToFrame(FrameType.HIGHSCORE);
				} , world);
		seedTF = mainMenuPanel.getSeedTF();
		seedTF.setText("" + new Random().nextInt());
		mainMenuPanel.setPreferredSize(getPrefSize(mainMenuPanel));
		JScrollPane outer = new JScrollPane(mainMenuPanel);
		outer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		mainMenuPane = outer;
	}

	public void buildGameOverGui() {
		GameOverPanel panel = new GameOverPanel();
		panel.setPreferredSize(getPrefSize(panel));
		panel.getAddHighscores().addActionListener((ActionEvent e) -> {
			changeToFrame(FrameType.MAIN_MENU);
			world.addToHighscoreList(panel.getNameText().getText());
			changeToFrame(FrameType.MAIN_MENU);
		});
		hsScoreInfo = panel.getHighScorezeigen();
		JScrollPane outer = new JScrollPane(panel);
		outer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		gameOverPane = outer;
	}

	public void buildHUD() {
		// First 0 is the View of the scrollPane and the second 0 is the JPanel
		viewportPanel = (JPanel) ((JPanel) ((JViewport) viewPortPane.getComponent(0)).getComponent(0)).getComponent(1);
		viewportPanel.setLayout(null);

		JLabel healthBackgroundLabel = new JLabel();
		ImageIcon bg = loadHUDImageIcon("health_bar_bg.png");
		healthBackgroundLabel.setIcon(bg);
		int posX = 10;
		int posY = 15;
		healthBackgroundLabel.setBounds(posX, posY, bg.getIconWidth(), bg.getIconHeight());
		viewportPanel.add(healthBackgroundLabel);

		healthBarLabel = new JLabel();
		healthBarImage = loadHUDBufferedImage("health_bar_bar.png");
		healthBarLabel.setIcon(new ImageIcon(healthBarImage));
		healthBarLabel.setBounds(posX + 42, posY, healthBarImage.getWidth(), healthBarImage.getHeight());
		viewportPanel.add(healthBarLabel);
		viewportPanel.setComponentZOrder(healthBarLabel, 0);

		JLabel scoreBackground = new JLabel();
		BufferedImage img = loadHUDBufferedImage("score_bg.png");
		scoreBackground.setIcon(new ImageIcon(img));
		posX = 10;
		posY += 45;
		scoreBackground.setBounds(posX, posY, img.getWidth(), img.getHeight());
		viewportPanel.add(scoreBackground);

		scoreLabel = new JLabel();
		scoreLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		scoreLabel.setForeground(Color.white);
		scoreLabel.setBounds(posX + 45, posY - 29, 107, 100);
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		viewportPanel.add(scoreLabel);
		viewportPanel.setComponentZOrder(scoreLabel, 0);

		JLabel weaponBulletBackground = new JLabel();
		img = loadHUDBufferedImage("weapon_bullets_bg.png");
		weaponBulletBackground.setIcon(new ImageIcon(img));
		posX = 20;
		posY += 50;
		weaponBulletBackground.setBounds(posX, posY, img.getWidth(), img.getHeight());
		viewportPanel.add(weaponBulletBackground);

		weaponLabel = new JLabel();
		weaponLabel.setBounds(posX - 8, posY - 35, 200, 100);
		viewportPanel.add(weaponLabel);
		viewportPanel.setComponentZOrder(weaponLabel, 0);

		ammoLabel = new JLabel();
		ammoLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		ammoLabel.setForeground(Color.white);
		ammoLabel.setBounds(posX + 75, posY - 35, 50, 100);
		ammoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		viewportPanel.add(ammoLabel);
		viewportPanel.setComponentZOrder(ammoLabel, 0);

		JLabel medipackBackground = new JLabel();
		img = loadHUDBufferedImage("medipack_bg.png");
		medipackBackground.setIcon(new ImageIcon(img));
		posX = 10;
		posY += 40;
		medipackBackground.setBounds(posX, posY, img.getWidth(), img.getHeight());
		viewportPanel.add(medipackBackground);

		mediPackLabel = new JLabel();
		mediPackLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		mediPackLabel.setForeground(Color.white);
		mediPackLabel.setBounds(posX + 6, posY - 31, 107, 100);
		mediPackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		viewportPanel.add(mediPackLabel);
		viewportPanel.setComponentZOrder(mediPackLabel, 0);

		JLabel timeBackground = new JLabel();
		img = loadHUDBufferedImage("time_bg.png");
		timeBackground.setIcon(new ImageIcon(img));
		posX = viewportPanel.getWidth() / 2 - img.getWidth() / 2;
		posY = 10;
		timeBackground.setBounds(posX, posY, img.getWidth(), img.getHeight());
		viewportPanel.add(timeBackground);

		timeLabel = new JLabel();
		timeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
		timeLabel.setForeground(Color.white);
		timeLabel.setBounds(posX, posY - 5, img.getWidth(), 100);
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		viewportPanel.add(timeLabel);
		viewportPanel.setComponentZOrder(timeLabel, 0);

		JLabel seedBackground = new JLabel();
		img = loadHUDBufferedImage("seed_bg.png");
		seedBackground.setIcon(new ImageIcon(img));
		posX = viewportPanel.getWidth() - 5 - img.getWidth();
		posY = 15;
		seedBackground.setBounds(posX, posY, img.getWidth(), img.getHeight());
		viewportPanel.add(seedBackground);

		seedLabel = new JLabel();
		seedLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		seedLabel.setForeground(Color.white);
		seedLabel.setBounds(posX + 12, posY - 28, img.getWidth(), 100);
		seedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		viewportPanel.add(seedLabel);
		viewportPanel.setComponentZOrder(seedLabel, 0);

		recalculateLabelPositions(0);
	}

	private ImageIcon loadHUDImageIcon(String name) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getClassLoader().getResource("images/hud/" + name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ImageIcon(img);
	}

	private BufferedImage loadHUDBufferedImage(String name) {
		return loadImage("images/hud/" + name);
	}

	private BufferedImage loadImage(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getClassLoader().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	public void updateMediPackLabel(int mediPacks) {
		mediPackLabel.setText("" + mediPacks);
	}

	private void recalculateLabelPositions(int startIndex) {
		Object[] jl = buffPanels.values().toArray();
		int MAX_BUFFS_NEXT_OTHER = 2;
		for (int i = startIndex; i < buffPanels.size(); i++) {
			((BuffPanel) jl[i]).setLocation(LABEL_X_START + (i % MAX_BUFFS_NEXT_OTHER) * SPACE_BETWEEN_LABELS,
					LABEL_Y_START + (i / MAX_BUFFS_NEXT_OTHER) * LABEL_HEIGHT);
		}
	}

	// Basically copied from WorldCanvas - thanks WorldCanvas!
	private Dimension getPrefSize(JPanel panel) {
		Dimension size = new Dimension();
		if (world != null) {
			size.width = WorldVisitor.getWidthInPixels(world);
			size.height = WorldVisitor.getHeightInPixels(world);
			Insets insets = panel.getInsets();
			size.width += insets.left + insets.right;
			size.height += insets.top + insets.bottom;
		}
		return size;
	}

	public void updateHealthLabel(int health, int maxHealth) {
		double percent = (health * 1.d / maxHealth);
		int width = (int) (healthBarImage.getWidth() * percent);
		if (width <= 0)
			return;
		healthBarLabel.setIcon(new ImageIcon(healthBarImage.getSubimage(0, 0, width, healthBarImage.getHeight())));
	}

	public void updateSeedLabel(int seed) {
		seedLabel.setText("" + seed);
	}

	public void updateScoreLabel(int score) {
		scoreLabel.setText(score + "");
	}

	public void updateAmmoLabel(Weapon w) {
		if (w == null)
			ammoLabel.setText("Error");
		else if (w instanceof LongRangeWeapon)
			ammoLabel.setText("" + ((LongRangeWeapon) w).getAmmo());
		else
			ammoLabel.setText("-");
	}

	public void updateWeaponName(Weapon w) {
		//String path = "src/images/enemies/weapons/" + w.getWeaponName() + "/" + w.getWeaponName() + "0.png";
		BufferedImage img = w.getIcon().getAwtImage();
		weaponLabel.setIcon(new ImageIcon(img.getScaledInstance(64, 32, BufferedImage.SCALE_DEFAULT)));
	}

	public void updateTimeLabel(int time) {
		time /= 1000.d;
		int seconds = time % 60;
		String sc = seconds < 10 ? "0" + seconds : "" + seconds;
		int minutes = time / 60;
		String min = minutes < 10 ? "0" + minutes : "" + minutes;
		timeLabel.setText(min + " : " + sc);
	}

	public void addOrUpdateBuffLabel(BuffType b, double param, int remainingTime, int maxTime) {
		String key = b.getValue();
		BuffPanel bPanel = buffPanels.get(key);
		if (bPanel == null) {
			bPanel = new BuffPanel(b.getValue());
			viewportPanel.add(bPanel);
			buffPanels.put(key, bPanel);
			recalculateLabelPositions(buffPanels.size() - 1);
		}
		bPanel.updateFlask((double) remainingTime / maxTime);
	}

	public void removeBuffLabel(BuffType b) {
		String key = b.getValue();
		BuffPanel bPanel = buffPanels.get(key);
		viewportPanel.remove(bPanel);
		buffPanels.remove(key);
		recalculateLabelPositions(0);
	}

	private class TestingModeActivator implements Action {
		private final static String activated = "Testing mode activated";
		private final static String deactivated = "Testing mode has been disabled";
		private JLabel testingModeNotifier = new JLabel();
		private Runnable run = () -> toggleNotification();
		ExecutorService ex = Executors.newSingleThreadExecutor();
		private Future<? extends Object> task;

		{
			testingModeNotifier.setVisible(true);
			testingModeNotifier.setEnabled(true);
			testingModeNotifier.setOpaque(true);
			testingModeNotifier.setBackground(Color.WHITE);
			testingModeNotifier.setBounds(DungeonMap.VIEWPORT_WIDTH / 2 - 100, 20, 200, 20);

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!world.isTestingMode()) {
				testingModeNotifier.setText(activated);
				world.setTestingMode(true);
			} else {
				testingModeNotifier.setText(deactivated);
				world.setTestingMode(false);
			}
			showNotification();
		}

		private void toggleNotification() {
			SwingUtilities.invokeLater(() -> {
				frame.getRootPane().add(testingModeNotifier, null, frame.getRootPane().getComponentCount() - 1);
				frame.revalidate();
				frame.repaint();
			});
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			} finally {
				SwingUtilities.invokeLater(() -> {
					frame.getRootPane().remove(testingModeNotifier);
					frame.revalidate();
					frame.repaint();
				});
			}
		}

		private void showNotification() {
			if (task != null) {
				task.cancel(true);
			}
			task = ex.submit(run);
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener arg0) {
		}

		@Override
		public Object getValue(String arg0) {
			return null;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public void putValue(String arg0, Object arg1) {
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener arg0) {
		}

		@Override
		public void setEnabled(boolean arg0) {
		}
	}
}
