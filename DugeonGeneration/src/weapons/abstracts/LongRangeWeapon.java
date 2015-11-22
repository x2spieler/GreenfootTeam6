package weapons.abstracts;

import java.awt.geom.Point2D;
import world.DungeonMap;

public abstract class LongRangeWeapon extends Weapon {
	private int ammo = -1;

	public LongRangeWeapon(int ammo) {
		isLongRangeWeapon = true;
		this.ammo = ammo;
	}

	@Override
	public void act() {
		super.act();
		if (launchLongRangeWeapon) {
			//Animation completed, launch bullet now
			Bullet b = instantiateBullet();
			Point2D offset = b.getCopyOfOffset();
			rotatePoint(offset, getRotation());
			//tryAddObject only adds the Bullet if it would be added to a legal tile. If Bullet were to no longer subclass DungeonMover this would stop working, however. Whatever happened to addBulletToWorld, btw?
			System.out.println("tryadd");
			if (((DungeonMap) getWorld()).tryAddObject(b, getGlobalX() + (int) offset.getX(), getGlobalY() + (int) offset.getY())) {
				b.setRotation(getRotation());
				System.out.println("added");
			}
			launchLongRangeWeapon = false;
		}
	}

	@Override
	protected boolean triggerUse() {
		if (ammo > 0) {
			ammo--;
			return true;
		}
		return false;
	}

	public void addAmmo(int ammo) {
		this.ammo += ammo;
	}

	public void removeAmmo(int ammo) {
		this.ammo -= ammo;
		if (this.ammo < 0)
			this.ammo = 0;
	}

	public int getAmmo() {
		return ammo;
	}

	protected abstract Bullet instantiateBullet();

	@SuppressWarnings("rawtypes")
	public abstract boolean areBulletsClassOf(Class c);
}
