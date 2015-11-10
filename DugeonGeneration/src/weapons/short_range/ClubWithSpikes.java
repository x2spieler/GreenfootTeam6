package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class ClubWithSpikes extends ShortRangeWeapon
{
	public ClubWithSpikes(ScrollActor owner)
	{
		damage=20;
		weaponName="club_spikes";
		displayName="Spike club";
		this.owner=owner;
		reloadTimeInMS=850;
		additionalValue=1;
		ticksPerAnimImg=8;
	}
}