package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class WoodStake extends ShortRangeWeapon
{
	public WoodStake(ScrollActor owner)
	{
		damage=3;
		weaponName="wood_stake";
		displayName="Wooden Stake";
		this.owner=owner;
		reloadTimeInMS=1400;
		additionalValue=1;
		ticksPerAnimImg=8;
	}
}
