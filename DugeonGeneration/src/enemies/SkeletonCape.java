package enemies;

import AI.Enemy;

public class SkeletonCape extends Enemy
{	
	public SkeletonCape()
	{
		setSpeed(175);
		value = 2;
		hp = 10;
		enemyName="skeleton_cape";
		allowedWeapons=new String[]{"bone"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
