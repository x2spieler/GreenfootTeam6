package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Axe extends ShortRangeWeapon
{
	public Axe(ScrollActor owner)
	{
		damage=15;
		weaponName="axe";
		displayName="Axe";
		this.owner=owner;
		reloadTimeInMS=1200;
		additionalValue=1;
		ticksPerAnimImg=8;
	}
}
