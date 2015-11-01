package enemies;

import java.util.Random;

import AI.Enemy;
import greenfoot.World;
import weapons.short_range.ClubWithSpikes;
import weapons.short_range.Sword;

public class Werewolf extends Enemy
{	
	public Werewolf()
	{
		stepsPerTick = 2;
		value = 1;
		hp = 50;
		viewRangeSquared = 32*32*5*5;
		enemyName="werewolf";
		loadImages();
		Random r=new Random();
		if(r.nextBoolean())
			weapon=new Sword(this);
		else
			weapon=new ClubWithSpikes(this);
	}

	@Override
	public void addedToWorld(World world)
	{
		world.addObject(weapon, 0, 0);
	}
}
