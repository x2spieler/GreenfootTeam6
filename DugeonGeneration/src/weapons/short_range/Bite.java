package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Bite extends ShortRangeWeapon
{
	public Bite(ScrollActor owner)
	{
		damage=6;
		weaponName="bite";
		displayName="Bite";
		this.owner=owner;
		reloadTimeInMS=2000;
		additionalValue=1;
		ticksPerAnimImg=5;
	}
}
