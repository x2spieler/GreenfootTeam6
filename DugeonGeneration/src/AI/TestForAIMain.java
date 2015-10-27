package AI;

import java.awt.Point;


public class TestForAIMain
{
	public static void main(String[] args)
	{
		Enemy e=new Enemy();
		e.findPath(new Point(2, 2), new Point(48, 48));
	}
}
