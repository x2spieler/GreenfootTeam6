package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Bite extends ShortRangeWeapon
{
	public Bite(ScrollActor owner)
	{
		damage=3;
		weaponName="bite";
		displayName="Bite";
		this.owner=owner;
		reloadTimeInMS=800;
		additionalValue=1;
		ticksPerAnimImg=5;
	}
}
