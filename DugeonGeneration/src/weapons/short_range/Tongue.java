package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Tongue extends ShortRangeWeapon
{
	public Tongue(ScrollActor owner)
	{
		damage=5;
		weaponName="tongue";
		displayName="Tongue";
		this.owner=owner;
		reloadTimeInMS=2000;
		additionalValue=1;
		ticksPerAnimImg=4;
	}
}
