package enemies.bullets;

import AI.Bullet;
import greenfoot.GreenfootImage;

public class Spear extends Bullet
{
	public Spear()
	{
		stepsPerFrame=4;
		damage=4;
		range=300;
		setImage(new GreenfootImage("enemies/bullets/arrow.png"));
	}
}
