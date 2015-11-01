package weapons.abstracts;
import java.util.List;

import AI.Enemy;
import AI.IDamageable;
import player.DeltaMover;
import player.Player;
import weapons.bullets.EntityType;

public abstract class Bullet extends DeltaMover
{
	protected int damage = -1;
	protected int lifetimeInMs = -1;
	private long timeStampCreated=-1;
	private EntityType typeToIgnore;
	
	public Bullet(EntityType typeToIgnore) 
	{
		super(0);
		timeStampCreated=System.currentTimeMillis();
		this.typeToIgnore=typeToIgnore;
	}

	public void act()
	{
		int currX=getGlobalX();
		int currY=getGlobalY();
		move();
		if ((currX==getGlobalX()&&currY==getGlobalY())||handleCollision() || timeStampCreated+lifetimeInMs<System.currentTimeMillis())
		{
			//Didn't move although move was called -> tried to move into wall || hit player/enemy || our time has come :(
			getWorld().removeObject(this);
		}
	}

	/**
	 * @return Returns true if we hit a player/enemy
	 */
	public boolean handleCollision()
	{
		List<?> intersectingObjects = getIntersectingObjects(null);
		if (intersectingObjects.size() != 0)
		{
			for (Object o : intersectingObjects)
			{
				if(typeToIgnore==EntityType.ENEMY&&(o instanceof Enemy)
						||typeToIgnore==EntityType.PLAYER&&(o instanceof Player))
					continue;

				IDamageable id=(IDamageable) o;
				id.damage(damage);
				return true;
			}
			return true;
		}
		return false;
	}
}
