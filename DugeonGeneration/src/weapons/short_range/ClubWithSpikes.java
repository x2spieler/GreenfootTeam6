package weapons.short_range;

import AI.Enemy;
import weapons.abstracts.ShortRangeWeapon;

public class ClubWithSpikes extends ShortRangeWeapon
{
	public ClubWithSpikes(Enemy user)
	{
		damage=20;
		weaponName="club_spikes";
		loadImages();
		this.user=user;
		reloadTimeInMS=3000;
	}
}