package enemies;

import AI.Enemy;

public class SkeletonCape extends Enemy
{	
	public SkeletonCape()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="skeleton_cape";
		allowedWeapons=new String[]{"flame_storm"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
