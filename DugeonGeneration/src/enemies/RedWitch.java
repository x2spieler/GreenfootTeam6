package enemies;

import AI.Enemy;

public class RedWitch extends Enemy
{	
	public RedWitch()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="red_witch";
		allowedWeapons=new String[]{"love_wand"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}