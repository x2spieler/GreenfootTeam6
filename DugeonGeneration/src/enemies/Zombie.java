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
		viewRangeSquared = 400*400;
		notChaseRangeSquared=16*16;
		setImage(new GreenfootImage("enemy.png"));
	}
}
