package enemies;

import AI.Enemy;

public class PurpleDemon extends Enemy
{	
	public PurpleDemon()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="purple_demon";
		allowedWeapons=new String[]{"flame_storm"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 12;
	}
}
