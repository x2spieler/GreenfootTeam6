package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class FlameStorm extends ShortRangeWeapon
{
	public FlameStorm(ScrollActor owner)
	{
		damage=26;
		weaponName="flame_storm";
		displayName="Flamestorm";
		this.owner=owner;
		reloadTimeInMS=4000;
		additionalValue=1;
		ticksPerAnimImg=5;
	}
}
