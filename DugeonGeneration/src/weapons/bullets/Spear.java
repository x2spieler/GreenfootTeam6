package weapons.bullets;

import greenfoot.GreenfootImage;
import weapons.abstracts.Bullet;

public class Spear extends Bullet
{
	public Spear(EntityType typeToIgnore)
	{
		super(typeToIgnore);
		damage=4;
		lifetimeInMs=4000;
		setImage(new GreenfootImage("enemies/bullets/arrow.png"));
		setSpeed(200);
	}
}
