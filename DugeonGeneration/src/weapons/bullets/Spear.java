package weapons.bullets;

import greenfoot.GreenfootImage;
import weapons.abstracts.Bullet;

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
