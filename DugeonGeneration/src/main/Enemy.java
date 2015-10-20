package main;
import greenfoot.Actor;

public class Enemy extends Actor implements IDamageable
{
	//cool
	//zweiter Test
	//cool
	//zweiter Test
	//Geiles Testkommentar
	//dsamodmasod
	//dlkasdklsanaslbm
	
	protected double velocity = -1;
	Weapon weapon = null;
	protected int value = -1;
	protected int hp = -1;
	protected int viewRange = -1;


	public void damage(int dmg)
	{
		hp -= dmg;
		if (hp <= 0)
		{
			IWorldInterfaceForAI wi = (IWorldInterfaceForAI) getWorld();
			if (wi != null)
				wi.addPlayerScore(value);
			else
				System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
		}
	}

	@Override
	public void act()
	{

	}

}
