package enemies;

import AI.Enemy;

public class Mummy extends Enemy
{	
	public Mummy()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		viewRangeSquared = 32*32*10*10;
		enemyName="mummy";
		allowedWeapons=new String[]{"sceptre"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
