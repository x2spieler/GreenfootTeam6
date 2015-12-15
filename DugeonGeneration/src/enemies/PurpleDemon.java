package enemies;

import AI.Enemy;

public class PurpleDemon extends Enemy
{	
	public PurpleDemon()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		enemyName="purple_demon";
		allowedWeapons=new String[]{"acid_bubbles"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 12;
	}
}
