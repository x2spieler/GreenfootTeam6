package weapons.long_range_weapon;

import scrollWorld.ScrollActor;
import weapons.abstracts.Bullet;
import weapons.abstracts.LongRangeWeapon;
import weapons.bullets.CrossbowArrow;

public class Crossbow extends LongRangeWeapon{
	
	public Crossbow(ScrollActor owner, int ammo)
	{
		super(ammo);
		damage=20;
		weaponName="crossbow";
		displayName="Crossbow";
		this.owner=owner;
		reloadTimeInMS=1200;
		additionalValue=1;
		ticksPerAnimImg=9;
	}

	@Override
	protected Bullet instantiateBullet() {
		return new CrossbowArrow(typeToIgnore);
	}

	@Override
	public boolean areBulletsClassOf(Class c) {
		return c==CrossbowArrow.class;
	}
}
