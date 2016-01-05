package enemies;

import AI.Enemy;

public class Snake extends Enemy
{	
	public Snake()
	{
		setSpeed(120);
		value = 2;
		hp = 10;
		enemyName="snake";
		allowedWeapons=new String[]{"tongue"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 16;
	}
}
