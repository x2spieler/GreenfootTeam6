package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Hammer extends ShortRangeWeapon
{
	public Hammer(ScrollActor owner)
	{
		damage=11;
		weaponName="hammer";
		displayName="Hammer";
		this.owner=owner;
		reloadTimeInMS=1200;
		additionalValue=1;
		ticksPerAnimImg=8;
	}
}
