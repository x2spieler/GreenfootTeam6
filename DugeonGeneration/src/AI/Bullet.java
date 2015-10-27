package AI;
import java.util.List;

import scrollWorld.ScrollActor;

public abstract class Bullet extends ScrollActor
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
	 * @return Returns true if a collision occurred
	 */
	//TODO: Don't use the greenfoot method for this
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
