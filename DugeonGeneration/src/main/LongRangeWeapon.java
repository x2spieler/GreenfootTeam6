package main;
public abstract class LongRangeWeapon extends Weapon
{

	@Override
	public void use()
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
	}

	protected abstract Bullet instantiateBullet();
}
