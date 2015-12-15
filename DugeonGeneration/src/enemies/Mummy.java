package enemies;

import AI.Enemy;

public class Mummy extends Enemy
{	
	public Mummy()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		enemyName="mummy";
		allowedWeapons=new String[]{"sceptre"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
