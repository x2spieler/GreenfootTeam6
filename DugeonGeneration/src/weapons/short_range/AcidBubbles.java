package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class AcidBubbles extends ShortRangeWeapon
{
	public AcidBubbles(ScrollActor owner)
	{
		damage=22;
		weaponName="acid_bubbles";
		displayName="Acid Bubbles";
		this.owner=owner;
		reloadTimeInMS=4000;
		additionalValue=1;
		ticksPerAnimImg=7;
	}
}
