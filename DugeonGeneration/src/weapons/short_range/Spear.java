package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Spear extends ShortRangeWeapon
{
	public Spear(ScrollActor owner)
	{
		damage=11;
		weaponName="spear";
		displayName="Spear";
		this.owner=owner;
		reloadTimeInMS=1000;
		additionalValue=1;
		ticksPerAnimImg=6;
	}
}
