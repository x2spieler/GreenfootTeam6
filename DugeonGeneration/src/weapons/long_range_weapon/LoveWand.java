package weapons.long_range_weapon;

import scrollWorld.ScrollActor;
import weapons.abstracts.Bullet;
import weapons.abstracts.LongRangeWeapon;
import weapons.bullets.CrossbowArrow;
import weapons.bullets.LoveShots;
import weapons.bullets.PurpleSpell;

public class LoveWand extends LongRangeWeapon{
	
	public LoveWand(ScrollActor owner, int ammo)
	{
		super(ammo);
		damage=20;
		weaponName="love_wand";
		displayName="Wand of love";
		this.owner=owner;
		reloadTimeInMS=1500;
		additionalValue=1;
		ticksPerAnimImg=4;
	}

	@Override
	protected Bullet instantiateBullet() {
		return new LoveShots(typeToIgnore);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean areBulletsClassOf(Class c) {
		return c==CrossbowArrow.class;
	}
}