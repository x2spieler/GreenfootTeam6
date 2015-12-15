package enemies;

import AI.Enemy;

public class Vampire extends Enemy
{	
	public Vampire()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		enemyName="vampire";
		allowedWeapons=new String[]{"wood_stake"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
