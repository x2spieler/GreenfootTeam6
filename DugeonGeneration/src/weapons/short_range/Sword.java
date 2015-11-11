package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Sword extends ShortRangeWeapon
{
	public Sword(ScrollActor owner)
	{
		damage=2000;
		weaponName="sword";
		displayName="Sword";
		this.owner=owner;
		reloadTimeInMS=750;
		additionalValue=1;
		ticksPerAnimImg=5;
	}
}
