package enemies;

import AI.Enemy;

public class Werewolf extends Enemy
{	
	public Werewolf()
	{
		setSpeed(175);
		value = 1;
		hp = 50;
		viewRangeSquared = 32*32*5*5;
		enemyName="werewolf";
		allowedWeapons=new String[]{"sword", "club_spikes"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
