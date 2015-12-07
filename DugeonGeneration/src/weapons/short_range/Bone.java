package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Bone extends ShortRangeWeapon
{
	public Bone(ScrollActor owner)
	{
		damage=3;
		weaponName="bone";
		displayName="Bone";
		this.owner=owner;
		reloadTimeInMS=800;
		additionalValue=1;
		ticksPerAnimImg=8;
	}
}
