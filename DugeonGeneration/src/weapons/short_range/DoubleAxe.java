package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class DoubleAxe extends ShortRangeWeapon
{
	public DoubleAxe(ScrollActor owner)
	{
		damage=24;
		weaponName="double_axe";
		displayName="Double Axe";
		this.owner=owner;
		reloadTimeInMS=4000;
		additionalValue=1;
		ticksPerAnimImg=10;
	}
}
