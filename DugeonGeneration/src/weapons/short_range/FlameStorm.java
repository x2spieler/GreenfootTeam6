package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class FlameStorm extends ShortRangeWeapon
{
	public FlameStorm(ScrollActor owner)
	{
		damage=15;
		weaponName="flame_storm";
		displayName="Flamestorm";
		this.owner=owner;
		reloadTimeInMS=750;
		additionalValue=1;
		ticksPerAnimImg=5;
	}
}
