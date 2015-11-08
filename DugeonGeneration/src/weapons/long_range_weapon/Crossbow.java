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
		this.owner=owner;
		reloadTimeInMS=2000;
	}

	@Override
	protected Bullet instantiateBullet() {
		return new CrossbowArrow(typeToIgnore);
	}

	@Override
	protected int getTicksPerAnimImg() {
		return 30;
	}

	@Override
	public boolean areBulletsClassOf(Class c) {
		return c==CrossbowArrow.class;
	}
}
