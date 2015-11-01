package weapons.bullets;

import java.awt.geom.Point2D;

import weapons.EntityType;
import weapons.abstracts.Bullet;

public class CrossbowArrow extends Bullet
{
	public CrossbowArrow(EntityType typeToIgnore)
	{
		super(typeToIgnore);
		damage=4;
		lifetimeInMs=4000;
		bulletName="crossbow_arrow";
		setSpeed(200);
		bulletOffsetFromPlayer=new Point2D.Double(32,0);
	}
}
