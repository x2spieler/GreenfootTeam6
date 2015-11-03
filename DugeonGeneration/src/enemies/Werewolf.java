package enemies;

import AI.Enemy;

public class Werewolf extends Enemy
{	
	public Werewolf()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="werewolf";
		allowedWeapons=new String[]{"ninja_star"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
