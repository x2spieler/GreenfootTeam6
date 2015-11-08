package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class Sword extends ShortRangeWeapon
{
	public Sword(ScrollActor owner)
	{
		damage=20;
		weaponName="sword";
		this.owner=owner;
		reloadTimeInMS=2000;
		additionalValue=1;
	}

	@Override
	protected int getTicksPerAnimImg() {
		return 10;
	}
}
