package weapons.long_range_weapon;

import scrollWorld.ScrollActor;
import weapons.abstracts.Bullet;
import weapons.abstracts.LongRangeWeapon;
import weapons.bullets.CrossbowArrow;
import weapons.bullets.PurpleSpell;

public class Sceptre extends LongRangeWeapon{
	
	public Sceptre(ScrollActor owner, int ammo)
	{
		super(ammo);
		damage=15;
		weaponName="sceptre";
		displayName="Sceptre";
		this.owner=owner;
		reloadTimeInMS=3500;
		additionalValue=1;
		ticksPerAnimImg=4;
	}

	@Override
	protected Bullet instantiateBullet() {
		return new PurpleSpell(typeToIgnore);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean areBulletsClassOf(Class c) {
		return c==CrossbowArrow.class;
	}
}
