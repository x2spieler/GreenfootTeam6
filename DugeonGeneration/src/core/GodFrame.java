package core;

import java.awt.Component;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class GodFrame 
{
	
	private JFrame frame;
	JScrollPane viewPortPane;		//ViewportPane is the component the viewport is drawn on - who would have guessed that?
	
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
	}
	
	public void addScrollListener(MouseWheelListener listener)
	{
		viewPortPane.addMouseWheelListener(listener);
	}

}
