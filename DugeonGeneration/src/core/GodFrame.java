package core;

import greenfoot.Greenfoot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


public class GodFrame 
{

	private JFrame frame;
	JScrollPane viewPortPane;		//ViewportPane is the component the viewport is drawn on - who would have guessed that?
	JScrollPane mainMenuPane=null;
	
	public GodFrame(JFrame frame)
	{
		this.frame=frame;
		for(Component c:frame.getContentPane().getComponents())
		{
			if(c instanceof JScrollPane)
			{
				viewPortPane=(JScrollPane)c;
				break;
			}
		}
		buildMainMenuGui();
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
			frame.getContentPane().remove(mainMenuPane);
			frame.getContentPane().add(viewPortPane, BorderLayout.CENTER);
			Greenfoot.start();
			break;
		case MAIN_MENU:
			frame.getContentPane().remove(viewPortPane);
			frame.getContentPane().add(mainMenuPane, BorderLayout.CENTER);
			Greenfoot.stop();
			break;
		}
		frame.revalidate();
		frame.repaint();
	}

	public void buildMainMenuGui()
	{
		JPanel panel=new JPanel();
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		///////CODE FOR MAIN MENU GOES HERE
		
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
		
		mainMenuPane=outer;
	}

}
