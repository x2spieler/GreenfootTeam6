package weapons.long_range_weapon;

import scrollWorld.ScrollActor;
import weapons.abstracts.Bullet;
import weapons.abstracts.LongRangeWeapon;

public class NinjaStar extends LongRangeWeapon
{

	public NinjaStar(ScrollActor owner) 
	{
		damage=20;
		weaponName="ninja_star";
		this.owner=owner;
		reloadTimeInMS=2000;
	}
	
	@Override
	protected Bullet instantiateBullet() {
		return new weapons.bullets.NinjaStar(typeToIgnore);
	}

	@Override
	protected int getTicksPerAnimImg() {
		return 10;
	}

}
