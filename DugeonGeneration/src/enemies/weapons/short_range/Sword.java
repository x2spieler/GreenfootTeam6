package enemies.weapons.short_range;

import java.awt.Point;

import AI.Enemy;
import AI.ShortRangeWeapon;

public class Sword extends ShortRangeWeapon
{
	public Sword(Enemy user)
	{
		damage=20;
		weaponName="sword";
		loadImages();
		this.user=user;
		reloadTimeInMS=2000;
	}
}
