package weapons.long_range_weapon;

import scrollWorld.ScrollActor;
import weapons.abstracts.Bullet;
import weapons.abstracts.LongRangeWeapon;
import weapons.bullets.CrossbowArrow;

public class NinjaStar extends LongRangeWeapon
{

	public NinjaStar(ScrollActor owner, int ammo)
	{
		super(ammo);
		damage=20;
		weaponName="ninja_star";
		displayName="Ninja star";
		this.owner=owner;
		reloadTimeInMS=500;
		additionalValue=1;
		ticksPerAnimImg=3;
	}
	
	@Override
	protected Bullet instantiateBullet() {
		return new weapons.bullets.NinjaStar(typeToIgnore);
	}

	@Override
	public boolean areBulletsClassOf(Class c) {
		return c==weapons.bullets.NinjaStar.class;
	}
}
