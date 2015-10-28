package AI;

import java.awt.Point;


public class TestForAIMain
{
	public static void main(String[] args)
	{
		/*Enemy e=new Enemy();
		e.findPath(new Point(2, 2), new Point(48, 48));*/
		System.out.println(rec(20));
	}
	
	private static double rec(int n)
	{
		if(n!=1)
			return 2.d-1.d/rec(n-1);
		return 2;
	}
}
//995672767
//659362109