package enemies;

import AI.Enemy;

public class Orc extends Enemy
{	
	public Orc()
	{
		setSpeed(175);
		value = 6;
		hp = 30;
		enemyName="orc";
		allowedWeapons=new String[]{"double_axe"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 16;
	}
}
