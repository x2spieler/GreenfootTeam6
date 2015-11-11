package core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import greenfoot.Greenfoot;
import greenfoot.WorldVisitor;
import weapons.abstracts.LongRangeWeapon;
import weapons.abstracts.Weapon;
import world.DungeonMap;


public class GodFrame 
{

	//TODO: Scrollen bei Viewport rausnehmen
	//TODO: HUD für Buffs
	
	private JFrame frame;
	JScrollPane viewPortPane;		//ViewportPane is the component the viewport is drawn on - who would have guessed that?
	JScrollPane pauseMenuPane=null;
	JScrollPane mainMenuPane=null;
	DungeonMap world;

	private JLabel healthLabel;
	private JLabel ammoLabel;
	private JLabel weaponLabel;
	private JLabel scoreLabel;
	private JLabel seedLabel;

	public GodFrame(JFrame frame, DungeonMap world)
	{
		this.frame=frame;
		this.world=world;
		frame.getContentPane().remove(1);		//Removes the greenfoot buttons
		viewPortPane=(JScrollPane)frame.getContentPane().getComponent(0);	//Component 0 is the JScrollPane containing the viewport
		buildMainMenuGui();
		buildMenuGui();	
		buildHUD();
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
			world.restart();
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
		}
		frame.pack();
	}
	
	private void changeTo(Component t)
	{
		frame.getContentPane().remove(viewPortPane);
		frame.getContentPane().remove(mainMenuPane);
		frame.getContentPane().remove(pauseMenuPane);
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
		});
		panel.add(resume);
		///////

		JScrollPane outer = new JScrollPane(panel);
		outer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		pauseMenuPane=outer;
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
		});
		panel.add(start);
		///////

		JScrollPane outer = new JScrollPane(panel);
		outer.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		mainMenuPane=outer;
	}

	public void buildHUD()
	{
		//First 0 is the View of the scrollPane and the second 0 is the JPanel
		JPanel vpPanel=(JPanel)((JPanel)((JViewport)viewPortPane.getComponent(0)).getComponent(0)).getComponent(1);
		vpPanel.setLayout(null);

		healthLabel=new JLabel("Health: -1");
		healthLabel.setForeground(new Color(255,0,0));
		healthLabel.setFont(new Font("Serif", Font.BOLD, 20));
		healthLabel.setBounds(25, 80, 250, 35);
		vpPanel.add(healthLabel);

		ammoLabel=new JLabel("Ammo: -1");
		ammoLabel.setForeground(new Color(255,0,0));
		ammoLabel.setFont(new Font("Serif", Font.BOLD, 20));
		ammoLabel.setBounds(25, 115, 250, 35);
		vpPanel.add(ammoLabel);

		weaponLabel=new JLabel("Weapon: -1");
		weaponLabel.setForeground(new Color(255,0,0));
		weaponLabel.setFont(new Font("Serif", Font.BOLD, 20));
		weaponLabel.setBounds(25, 150, 250, 35);
		vpPanel.add(weaponLabel);
		
		scoreLabel=new JLabel("Score: -1");
		scoreLabel.setForeground(new Color(255,0,0));
		scoreLabel.setFont(new Font("Serif", Font.BOLD, 20));
		scoreLabel.setBounds(25, 185, 250, 35);
		vpPanel.add(scoreLabel);
		
		seedLabel=new JLabel("Seed: -1");
		seedLabel.setForeground(new Color(255,0,0));
		seedLabel.setFont(new Font("Serif", Font.BOLD, 20));
		seedLabel.setBounds(25, 220, 250, 35);
		vpPanel.add(seedLabel);
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
		healthLabel.setText("Health: "+health);
	}
	
	public void updateSeedLabel(int seed)
	{
		seedLabel.setText("Seed: "+seed);
	}
	
	public void updateScoreLabel(int score)
	{
		scoreLabel.setText("Score: "+score);
	}

	public void updateAmmoLabel(Weapon w)
	{
		if(w==null)
			ammoLabel.setText("Error");
		else if(w instanceof LongRangeWeapon)
			ammoLabel.setText("Ammo: "+((LongRangeWeapon)w).getAmmo());
		else
			ammoLabel.setText("Ammo: -");
	}

	public void updateWeaponName(Weapon w)
	{
		if(w==null)
			weaponLabel.setText("Error");
		else
			weaponLabel.setText("Weapon: "+w.getDisplayName());
	}

}
