package enemies;

import AI.Enemy;

public class Zombie extends Enemy
{	
	public Zombie()
	{
		setSpeed(100);
		value = 4;
		hp = 20;
		enemyName="zombie";
		allowedWeapons=new String[]{"shovel"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 30;
	}
}
