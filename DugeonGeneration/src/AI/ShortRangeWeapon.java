package AI;

import java.awt.Point;

public abstract class ShortRangeWeapon extends Weapon
{

	protected final int MAX_ANGLE_ATTACK_DIF = 45;
	protected int range=-1;

	@Override
	public void use()
	{
		long millisNow = System.currentTimeMillis();
		if (lastUsage + reloadTimeInMS < millisNow)
		{
			lastUsage = millisNow;
			IWorldInterfaceForAI wi = (IWorldInterfaceForAI) getWorld();
			if (wi != null)
			{
				
				//TODO: Always play the animation, the following code is only to determine if we deal damage to the player
				
				Point playerCoords = wi.getPlayerPosition();
				double xDiff = playerCoords.x - getGlobalX();
				double yDiff = playerCoords.y - getGlobalY();
				
				//Check if enemy looks at player
				double angle = Math.toDegrees(Math.asin(xDiff / yDiff));
				double angleDiff = Math.abs(angle - getRotation());
				if (angleDiff > MAX_ANGLE_ATTACK_DIF)
					return;
				
				//Check if enemy is close enough to the player
				double distToPlayer=Math.sqrt(xDiff*xDiff+yDiff*yDiff);
				if(distToPlayer>range)
					return;
				
				wi.getPlayer().damage(damage);
			} else
				System.out.println("Can't cast world to WorldInterfaceForAI\nSomething's clearly wrong!");
		}
	}

}
