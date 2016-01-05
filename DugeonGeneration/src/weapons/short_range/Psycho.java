package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Psycho extends ShortRangeWeapon
{
	public Psycho(ScrollActor owner)
	{
		damage=14;
		weaponName="psycho";
		displayName="Psycho";
		this.owner=owner;
		reloadTimeInMS=3000;
		additionalValue=1;
		ticksPerAnimImg=7;
	}
}
