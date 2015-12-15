package enemies;

import AI.Enemy;

public class Goblin extends Enemy
{	
	public Goblin()
	{
		setSpeed(210);
		value = 1;
		hp = 1;
		enemyName="goblin";
		allowedWeapons=new String[]{"dagger"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 10;
	}
}
