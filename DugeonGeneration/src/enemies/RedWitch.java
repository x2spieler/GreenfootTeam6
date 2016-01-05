package enemies;

import AI.Enemy;

public class RedWitch extends Enemy
{	
	public RedWitch()
	{
		setSpeed(175);
		value = 4;
		hp = 20;
		enemyName="red_witch";
		allowedWeapons=new String[]{"love_wand"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
