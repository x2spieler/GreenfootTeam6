package enemies;

import AI.Enemy;
import greenfoot.GreenfootImage;

public class Werewolf extends Enemy
{	
	public Werewolf()
	{
		stepsPerTick = 2;
		value = 1;
		hp = 50;
		viewRangeSquared = 32*32*5*5;
		idleImage=new GreenfootImage("enemies/werewolf_idle.png");
		walk1Image=new GreenfootImage("enemies/werewolf_walk1.png");
		walk2Image=new GreenfootImage("enemies/werewolf_walk2.png");
	}
}
