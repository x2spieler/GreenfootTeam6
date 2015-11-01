package weapons.abstracts;

import AI.IWorldInterfaceForAI;

public abstract class LongRangeWeapon extends Weapon
{

	//TODO: Implement LongRangeWeapons
	@Override
	public boolean use()
	{
		long millisNow = System.currentTimeMillis();
		if (lastUsage + reloadTimeInMS < millisNow)
		{
			Bullet b = instantiateBullet();
			b.setRotation(getRotation());
			IWorldInterfaceForAI wi = (IWorldInterfaceForAI) getWorld();
			if (wi != null)
			{
				wi.addBulletToWorld(b);
				lastUsage = millisNow;
			}
				
			else
				System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
		}
		else
			return false;
		return true;
	}

	protected abstract Bullet instantiateBullet();
}
