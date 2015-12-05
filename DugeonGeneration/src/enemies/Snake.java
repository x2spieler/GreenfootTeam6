package enemies;

import AI.Enemy;

public class Snake extends Enemy
{	
	public Snake()
	{
		setSpeed(120);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="snake";
		allowedWeapons=new String[]{"flame_storm"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 16;
	}
}
