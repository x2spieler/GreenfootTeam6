package enemies;

import AI.Enemy;

public class Goblin extends Enemy
{	
	public Goblin()
	{
		setSpeed(210);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="goblin";
		allowedWeapons=new String[]{"flame_storm"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 10;
	}
}
