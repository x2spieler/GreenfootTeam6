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
		setSpeed(175);
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
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}

	@Override
	public void addedToWorld(World world)
	{
		super.addedToWorld(world);
		world.addObject(weapon, 0, 0);
	}
}
