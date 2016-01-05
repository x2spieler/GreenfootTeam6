package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Shovel extends ShortRangeWeapon
{
	public Shovel(ScrollActor owner)
	{
		damage=12;
		weaponName="shovel";
		displayName="Shovel";
		this.owner=owner;
		reloadTimeInMS=3000;
		additionalValue=1;
		ticksPerAnimImg=9;
	}
}
