package enemies;

import AI.Enemy;

public class RedDragon extends Enemy
{	
	public RedDragon()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		enemyName="red_dragon";
		allowedWeapons=new String[]{"flame_storm"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
