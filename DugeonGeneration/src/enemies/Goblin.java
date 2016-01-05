package enemies;

import AI.Enemy;

public class Goblin extends Enemy
{	
	public Goblin()
	{
		setSpeed(210);
		value = 2;
		hp = 10;
		enemyName="goblin";
		allowedWeapons=new String[]{"dagger"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 10;
	}
}
