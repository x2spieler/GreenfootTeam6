package enemies;

import AI.Enemy;

public class Bee extends Enemy
{	
	public Bee()
	{
		setSpeed(175);
		value = 1;
		hp = 1;
		enemyName="bee";
		allowedWeapons=new String[]{"sting"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
	
	@Override
	protected void alterImages()
	{
		for(int i=0;i<4;i++)
			images[i][ImageIndex.ATTACK.getValue()]=images[(i+2)%4][ImageIndex.WALK1.getValue()];
	}
}
