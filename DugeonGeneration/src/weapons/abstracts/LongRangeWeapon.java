package weapons.abstracts;

import java.awt.geom.Point2D;

import scrollWorld.ScrollWorld;

public abstract class LongRangeWeapon extends Weapon
{
	private boolean inUse=false;
	
	public LongRangeWeapon() {
		isLongRangeWeapon=true;
	}
	
	@Override
	public void act()
	{
		super.act();
		if(inUse&&getImage()==emptyImage)
		{
			//Animation completed, launch bullet now
			Bullet b=instantiateBullet();
			Point2D offset=b.getCopyOfOffset();
			rotatePoint(offset, getRotation());
			getWorld().addObject(b, getGlobalX()+(int)offset.getX(), getGlobalY()+(int)offset.getY());
			//b.setGlobalLocation(getGlobalX()+(int)offset.getX(), getGlobalY()+(int)offset.getY());
			System.out.println(b.getGlobalX()+" # "+b.getGlobalY());
			b.setRotation(getRotation());
			inUse=false;
		}
	}
	
	 @Override
	 protected void triggerUse()
	 {
		 inUse=true;
	 }

	protected abstract Bullet instantiateBullet();
}
