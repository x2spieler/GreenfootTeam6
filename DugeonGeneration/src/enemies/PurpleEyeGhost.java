package enemies;

import AI.Enemy;

public class PurpleEyeGhost extends Enemy
{	
	public PurpleEyeGhost()
	{
		setSpeed(175);
		value = 4;
		hp = 20;
		enemyName="purple_eye_ghost";
		allowedWeapons=new String[]{"psycho"};
	}
	
	@Override
	protected int getNumFramesChangeWalkImage() {
		return 20;
	}
}
