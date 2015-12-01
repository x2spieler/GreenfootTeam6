package weapons.long_range_weapon;

import scrollWorld.ScrollActor;
import weapons.abstracts.Bullet;
import weapons.abstracts.LongRangeWeapon;

public class LeafThrower extends LongRangeWeapon
{

	public LeafThrower(ScrollActor owner, int ammo)
	{
		super(ammo);
		damage=20;
		weaponName="leaf_thrower";
		displayName="Leaf thrower";
		this.owner=owner;
		reloadTimeInMS=500;
		additionalValue=1;
		ticksPerAnimImg=1;
	}
	
	@Override
	protected Bullet instantiateBullet() {
		return new weapons.bullets.Leafs(typeToIgnore);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean areBulletsClassOf(Class c) {
		return c==weapons.bullets.NinjaStar.class;
	}
}
