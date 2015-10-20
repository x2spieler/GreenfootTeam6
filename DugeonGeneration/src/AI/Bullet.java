package AI;
import java.util.List;

import AI.IDamageable;
import greenfoot.Actor;

public abstract class Bullet extends Actor
{
	protected int stepsPerFrame = -1;
	protected int damage = -1;
	protected int range = -1;

	public void act()
	{
		move(stepsPerFrame);
		range -= stepsPerFrame;
		if (handleCollision() || range <= 0)
		{
			getWorld().removeObject(this);
		}
	}

	/**
	 * @return Returns if a collision occurred
	 */
	public boolean handleCollision()
	{
		List<?> intersectingObjects = getIntersectingObjects(null);
		if (intersectingObjects.size() != 0)
		{
			for (Object o : intersectingObjects)
			{
				IDamageable dmgAble = (IDamageable) o;
				if (dmgAble != null)
				{
					dmgAble.damage(damage);
					return true;
				}
			}
			return true;
		}
		return false;
	}
}
