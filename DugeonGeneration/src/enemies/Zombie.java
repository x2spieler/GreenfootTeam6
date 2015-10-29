package enemies;

import AI.Enemy;
import greenfoot.GreenfootImage;

public class Zombie extends Enemy
{	
	public Zombie()
	{
		stepsPerTick = 4;
		value = 1;
		hp = 50;
		viewRangeSquared = 32*32*5*5;
		setImage(new GreenfootImage("enemy.png"));
	}
}
