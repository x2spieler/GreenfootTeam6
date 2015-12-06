package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Sting extends ShortRangeWeapon
{
	public Sting(ScrollActor owner)
	{
		damage=5;
		weaponName="sting";
		displayName="Sting";
		this.owner=owner;
		reloadTimeInMS=750;
		additionalValue=1;
		ticksPerAnimImg=5;
	}
}
