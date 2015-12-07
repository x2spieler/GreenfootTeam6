package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class AcidBubbles extends ShortRangeWeapon
{
	public AcidBubbles(ScrollActor owner)
	{
		damage=5;
		weaponName="acid_bubbles";
		displayName="Acid Bubbles";
		this.owner=owner;
		reloadTimeInMS=750;
		additionalValue=1;
		ticksPerAnimImg=7;
	}
}
