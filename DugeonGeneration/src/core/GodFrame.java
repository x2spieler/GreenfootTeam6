package core;

import greenfoot.Greenfoot;
import greenfoot.WorldVisitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import world.DungeonMap;


public class GodFrame 
{

	private JFrame frame;
	JScrollPane viewPortPane;		//ViewportPane is the component the viewport is drawn on - who would have guessed that?
	JScrollPane pauseMenuPane=null;
	JScrollPane mainMenuPane=null;
	DungeonMap world;

	public GodFrame(JFrame frame, DungeonMap world)
	{
		this.frame=frame;
		this.world=world;
		for(Component c:frame.getContentPane().getComponents())
		{
			if(c instanceof JScrollPane)
			{
				viewPortPane=(JScrollPane)c;
				break;
			}
		}
		buildMainMenuGui();
		buildMenuGui();	
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
			frame.getContentPane().remove(pauseMenuPane);
			frame.getContentPane().remove(mainMenuPane);
			frame.getContentPane().add(viewPortPane, BorderLayout.CENTER);
			Greenfoot.start();
			break;
		case MAIN_MENU:
			frame.getContentPane().remove(viewPortPane);
			frame.getContentPane().remove(pauseMenuPane);
			frame.getContentPane().add(mainMenuPane, BorderLayout.CENTER);
			Greenfoot.stop();
			break;
		case PAUSE_MENU:
			frame.getContentPane().remove(viewPortPane);
			frame.getContentPane().remove(mainMenuPane);
			frame.getContentPane().add(pauseMenuPane, BorderLayout.CENTER);
			Greenfoot.stop();
			break;
		}
		frame.revalidate();
		frame.repaint();
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

}
