package enemies;

import AI.Enemy;

public class BlueFlower extends Enemy
{	
	public BlueFlower()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="blue_flower";
		allowedWeapons=new String[]{"leaf_thrower"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}