package weapons.short_range;

import scrollWorld.ScrollActor;
import weapons.abstracts.ShortRangeWeapon;

public class ClubWithSpikes extends ShortRangeWeapon
{
	public ClubWithSpikes(ScrollActor owner)
	{
		damage=20;
		weaponName="club_spikes";
		this.owner=owner;
		reloadTimeInMS=3000;
	}

	@Override
	protected int getTicksPerAnimImg() {
		return 10;
	}
}