package enemies;

import AI.Enemy;

public class Zombie extends Enemy
{	
	public Zombie()
	{
		setSpeed(100);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="zombie";
		allowedWeapons=new String[]{"shovel"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 30;
	}
}
