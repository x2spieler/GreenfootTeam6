package enemies;

import AI.Enemy;

public class Bee extends Enemy
{	
	public Bee()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="bee";
		allowedWeapons=new String[]{"flame_storm"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
