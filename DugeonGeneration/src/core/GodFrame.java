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
import java.awt.event.MouseWheelListener;
import java.util.LinkedHashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.WorldVisitor;
import menu.BuyItem;
import menu.ShopEntry;
import player.BuffType;
import weapons.abstracts.LongRangeWeapon;
import weapons.abstracts.Weapon;
import world.DungeonMap;


public class GodFrame 
{

	private JFrame frame;
	private JScrollPane viewPortPane;		//ViewportPane is the component the viewport is drawn on - who would have guessed that?
	private JScrollPane pauseMenuPane=null;
	private JScrollPane mainMenuPane=null;
	private JScrollPane gameOverPane=null;
	private JScrollPane nextRoundPane=null;
	private JPanel viewportPanel=null;
	private JLabel coinLabel=null;
	private JLabel buyFeedbackLabel=null;
	private JLabel timeLabel=null;
	private JLabel mediPackLabel=null;
	private DungeonMap world;

	private LinkedHashMap<String, JLabel>labels; 
	private final int LABEL_WIDTH=300;
	private final int LABEL_HEIGHT=20;
	private final int SPACE_BETWEEN_LABELS=20;
	private final int LABEL_X_START=20;
	private final int LABEL_Y_START=100;
	private final Color LABEL_FONT_COLOR=Color.red;
	private final Font LABEL_FONT;
	private JTextField seedTF;
	
	enum LabelType
	{
		HEALTH_LABEL("health"),
		AMMO_LABEL("ammo"),
		WEAPON_LABEL("weapon"),
		SCORE_LABEL("score"),
		SEED_LABEL("seed"),
		BUFF_LABEL("buff");

		private String val;

		private LabelType(String v)
		{
			this.val=v;
		}

		public String getValue()
		{
			return val;
		}
	}

	public GodFrame(JFrame frame, DungeonMap world)
	{
		this.frame=frame;
		this.world=world;
		labels=new LinkedHashMap<String, JLabel>();
		LABEL_FONT=new Font("Serif", Font.BOLD, 20);

		frame.getContentPane().remove(1);		//Removes the greenfoot buttons
		viewPortPane=(JScrollPane)frame.getContentPane().getComponent(0);	//Component 0 is the JScrollPane containing the viewport
		buildMainMenuGui();
		buildMenuGui();	
		buildHUD();
		buildGameOverGui();
		buildNextRoundGui();
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		frame.pack();
		frame.setLocation(screenSize.width/2-frame.getWidth()/2, screenSize.height/2-frame.getHeight()/2);
	}

	public void addScrollListener(MouseWheelListener listener)
	{
		viewPortPane.addMouseWheelListener(listener);
	}

	public void changeToFrame(FrameType type)
	{
		switch(type)
		{
		case VIEWPORT:
			changeTo(viewPortPane);
			Greenfoot.start();
			break;
		case MAIN_MENU:
			changeTo(mainMenuPane);
			Greenfoot.stop();
			break;
		case PAUSE_MENU:
			changeTo(pauseMenuPane);
			Greenfoot.stop();
			break;
		case GAME_OVER:
			changeTo(gameOverPane);
			Greenfoot.stop();
			break;
		case NEXT_ROUND:
			changeTo(nextRoundPane);
			buyFeedbackLabel.setText("");
			Greenfoot.stop();
			break;
		}
		frame.pack();
		frame.revalidate();
		frame.repaint();
	}

	private void changeTo(Component t)
	{
		frame.getContentPane().remove(viewPortPane);
		frame.getContentPane().remove(mainMenuPane);
		frame.getContentPane().remove(pauseMenuPane);
		frame.getContentPane().remove(gameOverPane);
		frame.getContentPane().remove(nextRoundPane);
		frame.getContentPane().add(t);
	}

	public void buildMenuGui()
	{
		JPanel panel=new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(getPrefSize(panel));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Dimension panelSize=panel.getPreferredSize();
		///////CODE FOR GAME OVER MENU GOES HERE

		JLabel pauseLabel=new JLabel("Game paused");
		pauseLabel.setBounds(0,100,panelSize.width,50);
		pauseLabel.setFont(new Font("", Font.PLAIN, 26));
		pauseLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(pauseLabel);

		JButton resume=new JButton();
		int buttonWidth=150;
		int buttonHeight=35;
		resume.setBounds(panelSize.width/2-buttonWidth/2,panelSize.height/2,buttonWidth,buttonHeight);
		resume.setText("Resume");
		resume.addActionListener((ActionEvent e)->{
			changeToFrame(FrameType.VIEWPORT);
		});
		panel.add(resume);
		///////

		JScrollPane outer = new JScrollPane(panel);
		outer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		pauseMenuPane=outer;
		
	}
	
	public void buildNextRoundGui()
	{
		JPanel panel=new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(getPrefSize(panel));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Dimension panelSize=panel.getPreferredSize();
		///////CODE FOR MENU GOES HERE
		
		JLabel wonLabel=new JLabel("You won the round!");
		wonLabel.setBounds(0,25,panelSize.width,50);
		wonLabel.setFont(new Font("", Font.ITALIC, 24));
		wonLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(wonLabel);
		
		JPanel entryPanel=new JPanel();
		entryPanel.setLayout(new GridLayout(0,1));
		int horizontalSpace=20;
		
		entryPanel.add(new ShopEntry(BuyItem.WEAPON_CROSSBOW, new GreenfootImage("enemies/weapons/crossbow/crossbow0.png"), 30, 1, world));
		entryPanel.add(new ShopEntry(BuyItem.WEAPON_SWORD, new GreenfootImage("enemies/weapons/sword/sword0.png"), 30, 1, world));
		entryPanel.add(new ShopEntry(BuyItem.WEAPON_CLUB_WITH_SPIKES, new GreenfootImage("enemies/weapons/club_spikes/club_spikes0.png"), 30, 1, world));
		entryPanel.add(new ShopEntry(BuyItem.WEAPON_NINJA_STAR, new GreenfootImage("enemies/weapons/ninja_star/ninja_star0.png"), 30, 1, world));
		entryPanel.add(new ShopEntry(BuyItem.BULLET_CROSSBOW_ARROW, new GreenfootImage("enemies/bullets/crossbow_arrow.png"), 30, 100, world));
		entryPanel.add(new ShopEntry(BuyItem.BULLET_NINJA_STAR, new GreenfootImage("enemies/bullets/ninja_star.png"), 30, 100, world));
		entryPanel.add(new ShopEntry(BuyItem.MEDI_PACK, new GreenfootImage("medi_pack.png"), 30, 1, world));
		
		JScrollPane scrollPane=new JScrollPane(entryPanel);
		int scrollY=100;
		int scrollHeight=520;
		scrollPane.setBounds(new Rectangle(horizontalSpace,scrollY,panelSize.width-2*horizontalSpace,scrollHeight));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane);
		
		coinLabel=new JLabel();
		updateCoinLabelInShop();
		coinLabel.setBounds(horizontalSpace+10, scrollY+scrollHeight+10, 150, 40);
		panel.add(coinLabel);
		
		buyFeedbackLabel=new JLabel();
		buyFeedbackLabel.setBounds(0, scrollY+scrollHeight+10, panelSize.width, 40);
		buyFeedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(buyFeedbackLabel);
		
		JButton resume=new JButton();
		int buttonWidth=150;
		int buttonHeight=40;
		resume.setBounds(panelSize.width/2-buttonWidth/2,panelSize.height-buttonHeight-50,buttonWidth,buttonHeight);
		resume.setText("Next round");
		resume.addActionListener((ActionEvent e)->{
			world.startNewRound();
			changeToFrame(FrameType.VIEWPORT);
		});
		panel.add(resume);
		
		///////

		JScrollPane outer = new JScrollPane(panel);
		outer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		nextRoundPane=outer;
	}

	public void updateCoinLabelInShop()
	{
		if(world!=null&&world.getPlayer()!=null)
		coinLabel.setText("Coins: "+world.getPlayer().getScore());
	}
	
	public void updateFeedbackLabel(boolean success, String msg)
	{
		if(success)
			buyFeedbackLabel.setForeground(new Color(0,150,0));
		else
			buyFeedbackLabel.setForeground(new Color(184,0,0));
		buyFeedbackLabel.setText(msg);
	}

	public void buildMainMenuGui()
	{
		JPanel panel=new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(getPrefSize(panel));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Dimension panelSize=panel.getPreferredSize();
		///////CODE FOR MAIN MENU GOES HERE

		JLabel startLabel=new JLabel("Start a new game");
		startLabel.setBounds(0,100,panelSize.width,50);
		startLabel.setFont(new Font("", Font.BOLD, 24));
		startLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(startLabel);

		JButton start=new JButton();
		int buttonWidth=150;
		int buttonHeight=40;
		start.setBounds(panelSize.width/2-buttonWidth/2,panelSize.height/2-60,buttonWidth,buttonHeight);
		start.setText("Start");
		start.addActionListener((ActionEvent e)->{
			changeToFrame(FrameType.VIEWPORT);
			world.startNewGame(Integer.valueOf(seedTF.getText()));
			seedTF.setText(""+new Random().nextInt());	//Already set seed for the next game
		});
		panel.add(start);
		
		JLabel seedLabel=new JLabel("Seed:");
		int lWidth=100;
		int lHeight=50;
		int space=20;
		seedLabel.setBounds(panelSize.width/2-lWidth-space,panelSize.height/2+150-lHeight/2,lWidth,lHeight);
		seedLabel.setHorizontalAlignment(JLabel.RIGHT);
		panel.add(seedLabel);
		
		seedTF=new JTextField(""+new Random().nextInt());
		int tfWidth=100;
		int tfHeight=24;
		seedTF.setBounds(panelSize.width/2+space,panelSize.height/2+150-tfHeight/2,tfWidth,tfHeight);
		panel.add(seedTF);
		///////

		JScrollPane outer = new JScrollPane(panel);
		outer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		mainMenuPane=outer;
	}
	
	public void buildGameOverGui()
	{
		JPanel panel=new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(getPrefSize(panel));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Dimension panelSize=panel.getPreferredSize();
		///////CODE FOR GAME OVER MENU GOES HERE

		JLabel loseLabel=new JLabel("You lost!");
		loseLabel.setBounds(0,100,panelSize.width,50);
		loseLabel.setFont(new Font("", Font.ITALIC, 24));
		loseLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(loseLabel);

		JButton mmenu=new JButton();
		int buttonWidth=200;
		int buttonHeight=30;
		mmenu.setBounds(panelSize.width/2-buttonWidth/2,panelSize.height/2,buttonWidth,buttonHeight);
		mmenu.setText("Back to main menu");
		mmenu.addActionListener((ActionEvent e)->{
			changeToFrame(FrameType.MAIN_MENU);
		});
		panel.add(mmenu);
		///////

		JScrollPane outer = new JScrollPane(panel);
		outer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		gameOverPane=outer;
	}

	public void buildHUD()
	{
		//First 0 is the View of the scrollPane and the second 0 is the JPanel
		viewportPanel=(JPanel)((JPanel)((JViewport)viewPortPane.getComponent(0)).getComponent(0)).getComponent(1);
		viewportPanel.setLayout(null);

		JLabel healthLabel=createHUDLabelAndAddToPanel("Health: -1");
		labels.put(LabelType.HEALTH_LABEL.getValue(), healthLabel);

		JLabel ammoLabel=createHUDLabelAndAddToPanel("Ammo: -1");
		labels.put(LabelType.AMMO_LABEL.getValue(), ammoLabel);

		JLabel weaponLabel=createHUDLabelAndAddToPanel("Weapon: -1");
		labels.put(LabelType.WEAPON_LABEL.getValue(), weaponLabel);

		JLabel scoreLabel=createHUDLabelAndAddToPanel("Score: -1");
		labels.put(LabelType.SCORE_LABEL.getValue(), scoreLabel);

		JLabel seedLabel=createHUDLabelAndAddToPanel("Seed: -1");
		labels.put(LabelType.SEED_LABEL.getValue(), seedLabel);
		
		mediPackLabel=createHUDLabelAndAddToPanel("Medipacks: -1");
		mediPackLabel.setBounds(850,100,150,50);

		recalculateLabelPositions(0);
		
		timeLabel=new JLabel("00:00");
		Dimension d=viewportPanel.getPreferredSize();
		int tlWidth=200;
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		timeLabel.setBounds((int)(d.getWidth()/2-tlWidth/2), 20, tlWidth, 50);
		timeLabel.setForeground(LABEL_FONT_COLOR);
		timeLabel.setFont(new Font("Serif", Font.BOLD, 26));
		viewportPanel.add(timeLabel);
	}
	
	public void updateMediPackLabel(int mediPacks)
	{
		mediPackLabel.setText("Medipacks: "+mediPacks);
	}

	private JLabel createHUDLabelAndAddToPanel(String text)
	{
		JLabel label=new JLabel(text);
		label.setForeground(LABEL_FONT_COLOR);
		label.setFont(LABEL_FONT);
		viewportPanel.add(label);
		return label;
	}

	private void recalculateLabelPositions(int startIndex)
	{
		Object[] jl=labels.values().toArray();
		for(int i=startIndex;i<labels.size();i++)
		{
			((JLabel)jl[i]).setBounds(LABEL_X_START, LABEL_Y_START+i*(LABEL_HEIGHT+SPACE_BETWEEN_LABELS), LABEL_WIDTH, LABEL_HEIGHT);
		}
	}

	//Basically copied from WorldCanvas - thanks WorldCanvas!
	private Dimension getPrefSize(JPanel panel)
	{
		Dimension size=new Dimension();
		if (world != null) {
			size.width = WorldVisitor.getWidthInPixels(world) ;
			size.height = WorldVisitor.getHeightInPixels(world) ;
			Insets insets = panel.getInsets();
			size.width += insets.left + insets.right;
			size.height += insets.top + insets.bottom;
		}
		return size;
	}

	public void updateHealthLabel(int health)
	{
		labels.get(LabelType.HEALTH_LABEL.getValue()).setText("Health: "+health);
	}

	public void updateSeedLabel(int seed)
	{
		labels.get(LabelType.SEED_LABEL.getValue()).setText("Seed: "+seed);
	}

	public void updateScoreLabel(int score)
	{
		labels.get(LabelType.SCORE_LABEL.getValue()).setText("Score: "+score);
	}

	public void updateAmmoLabel(Weapon w)
	{
		if(w==null)
			labels.get(LabelType.AMMO_LABEL.getValue()).setText("Error");
		else if(w instanceof LongRangeWeapon)
			labels.get(LabelType.AMMO_LABEL.getValue()).setText("Ammo: "+((LongRangeWeapon)w).getAmmo());
		else
			labels.get(LabelType.AMMO_LABEL.getValue()).setText("Ammo: -");
	}

	public void updateWeaponName(Weapon w)
	{
		if(w==null)
			labels.get(LabelType.WEAPON_LABEL.getValue()).setText("Error");
		else
			labels.get(LabelType.WEAPON_LABEL.getValue()).setText("Weapon: "+w.getDisplayName());
	}
	
	public void updateTimeLabel(int time)
	{
		time/=1000.d;
		int seconds=time%60;
		String sc=seconds<10?"0"+seconds:""+seconds;
		int minutes=time/60;
		String min=minutes<10?"0"+minutes:""+minutes;
		timeLabel.setText(min+" : "+sc);
	}

	public void addOrUpdateBuffLabel(BuffType b, double param, int remainingTime)
	{
		String key=LabelType.BUFF_LABEL.getValue()+b.getValue();
		JLabel label=labels.get(key);
		if(label==null)
		{
			label=createHUDLabelAndAddToPanel("Lorem ipsum");
			labels.put(key, label);
			recalculateLabelPositions(labels.size()-1);
		}
		String txt=b.getValue()+(param*100)+" %";
		if(remainingTime!=-1)
			txt+=" , "+remainingTime/10+"."+remainingTime%10+"s";
		label.setText(txt);
	}

	public void removeBuffLabel(BuffType b)
	{
		String key=LabelType.BUFF_LABEL.getValue()+b.getValue();
		JLabel label=labels.get(key);
		viewportPanel.remove(label);
		labels.remove(key);
		recalculateLabelPositions(0);
	}
}
