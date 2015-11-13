package core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import greenfoot.Greenfoot;
import greenfoot.WorldVisitor;
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
	private DungeonMap world;

	private JLabel timeLabel;
	private LinkedHashMap<String, JLabel>labels; 
	private final int LABEL_WIDTH=300;
	private final int LABEL_HEIGHT=20;
	private final int SPACE_BETWEEN_LABELS=20;
	private final int LABEL_X_START=20;
	private final int LABEL_Y_START=100;
	private final Color LABEL_FONT_COLOR=Color.red;
	private final Font LABEL_FONT;
	
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
			viewPortPane.requestFocus();
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
			Greenfoot.start();
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

		///////CODE FOR MENU GOES HERE

		JButton resume=new JButton();
		resume.setBounds(400,200,100,30);
		resume.setText("Resume");
		resume.addActionListener((ActionEvent e)->{
			changeToFrame(FrameType.VIEWPORT);
			world.resume();
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

		///////CODE FOR MENU GOES HERE

		JButton resume=new JButton();
		resume.setBounds(400,200,100,30);
		resume.setText("Next round");
		resume.addActionListener((ActionEvent e)->{
			changeToFrame(FrameType.VIEWPORT);
			world.startNewRound();
		});
		panel.add(resume);
		///////

		JScrollPane outer = new JScrollPane(panel);
		outer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		nextRoundPane=outer;
	}


	public void buildMainMenuGui()
	{
		JPanel panel=new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(getPrefSize(panel));
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		///////CODE FOR MAIN MENU GOES HERE

		JButton start=new JButton();
		start.setBounds(400,200,100,30);
		start.setText("Start");
		start.addActionListener((ActionEvent e)->{
			changeToFrame(FrameType.VIEWPORT);
			world.startNewGame();
		});
		panel.add(start);
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

		///////CODE FOR GAME OVER MENU GOES HERE

		JButton start=new JButton();
		start.setBounds(400,200,200,30);
		start.setText("Back to main menu");
		start.addActionListener((ActionEvent e)->{
			changeToFrame(FrameType.MAIN_MENU);
		});
		panel.add(start);
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
		System.out.println(remainingTime);
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
