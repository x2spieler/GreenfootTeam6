package weapons.abstracts;
import java.awt.geom.Point2D;
import java.util.List;

import AI.Enemy;
import AI.IDamageable;
import greenfoot.World;
import player.DeltaMover;
import player.Player;
import weapons.EntityType;
import world.DungeonMap;

public abstract class Bullet extends DeltaMover
{
	protected int damage = -1;
	protected int lifetimeInMs = -1;
	protected String bulletName="";
	protected Point2D bulletOffsetFromPlayer;
	private long timeStampCreated=-1;
	private EntityType typeToIgnore;

	public Bullet(EntityType typeToIgnore) 
	{
		super(0);
		timeStampCreated=DungeonMap.getGreenfootTime();
		this.typeToIgnore=typeToIgnore;
		bulletOffsetFromPlayer=new Point2D.Double(32,0);;
	}

	@Override
	public void addedToWorld(World w)
	{
		super.addedToWorld(w);
		setImage("enemies/bullets/"+bulletName+".png");
	}

	@Override
	public void act()
	{
		super.act();
		move();
		if (isTouchingWall()||handleCollision() || timeStampCreated+lifetimeInMs<DungeonMap.getGreenfootTime())
		{
			//Didn't move although move was called -> tried to move into wall. If we are at a rotation of x*90�, we will just stay in front of the wall until our lifetime is over || hit player/enemy || our time has come :(
			getWorld().removeObject(this);
		}
	}

	public Point2D getCopyOfOffset()
	{
		return new Point2D.Double(bulletOffsetFromPlayer.getX(), bulletOffsetFromPlayer.getY());
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
				if((typeToIgnore==EntityType.ENEMY&&(o instanceof Enemy))
						||(typeToIgnore==EntityType.PLAYER&&(o instanceof Player)))
					continue;

				if(!(o instanceof IDamageable))
					continue;

				IDamageable id=(IDamageable) o;
				id.damage(damage);
				return true;
			}
			return false;
		}
		return false;
	}
}
