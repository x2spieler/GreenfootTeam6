package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Sword extends ShortRangeWeapon
{
	public Sword(ScrollActor owner)
	{
		damage=20;
		weaponName="sword";
		loadImages();
		this.owner=owner;
		reloadTimeInMS=2000;
	}
}
