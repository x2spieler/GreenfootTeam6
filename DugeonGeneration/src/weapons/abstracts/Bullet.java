package weapons.abstracts;
import java.awt.geom.Point2D;
import java.util.List;

import AI.Enemy;
import AI.IDamageable;
import greenfoot.World;
import player.DeltaMover;
import player.Player;
import weapons.EntityType;

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
		timeStampCreated=System.currentTimeMillis();
		this.typeToIgnore=typeToIgnore;
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
		int currX=getGlobalX();
		int currY=getGlobalY();
		move();
		if ((currX==getGlobalX()&&currY==getGlobalY())||handleCollision() || timeStampCreated+lifetimeInMs<System.currentTimeMillis())
		{
			//Didn't move although move was called -> tried to move into wall || hit player/enemy || our time has come :(
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
		//TODO: Fix collision to ignore typeToIgnore
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
			return true;
		}
		return false;
	}
}