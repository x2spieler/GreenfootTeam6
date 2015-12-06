package enemies;

import AI.Enemy;

public class PurpleWorm extends Enemy
{	
	public PurpleWorm()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="purple_worm";
		allowedWeapons=new String[]{"bite"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 5;
	}
}
