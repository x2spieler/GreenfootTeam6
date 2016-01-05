package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Dagger extends ShortRangeWeapon
{
	public Dagger(ScrollActor owner)
	{
		damage=7;
		weaponName="dagger";
		displayName="Dagger";
		this.owner=owner;
		reloadTimeInMS=2000;
		additionalValue=1;
		ticksPerAnimImg=5;
	}
}
