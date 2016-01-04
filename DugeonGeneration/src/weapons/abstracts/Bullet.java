package weapons.abstracts;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import AI.Enemy;
import AI.IDamageable;
import greenfoot.World;
import player.DeltaMover;
import player.Player;
import weapons.EntityType;
import world.DungeonMap;

public abstract class Bullet extends DeltaMover {
	protected int damage = -1;
	protected int lifetimeInMs = -1;
	protected String bulletName = "";
	protected Point2D bulletOffsetFromPlayer;
	protected int maxHits = 1;
	private long timeStampCreated = -1;
	private EntityType typeToIgnore;
	private int hits = 0;
	private ArrayList<IDamageable> hitEntities;

	public Bullet(EntityType typeToIgnore) {
		super(0);
		timeStampCreated = DungeonMap.getGreenfootTime();
		this.typeToIgnore = typeToIgnore;
		bulletOffsetFromPlayer = new Point2D.Double(32, 0);
		hitEntities = new ArrayList<IDamageable>();
	}

	@Override
	public void addedToWorld(World w) {
		super.addedToWorld(w);
		setImage("enemies/bullets/" + bulletName + ".png");
	}

	@Override
	public void act() {
		super.act();
		move();
		if (handleCollision()) {
			hits++;
		}
		if (hits == maxHits || isTouchingWall() || timeStampCreated + lifetimeInMs < DungeonMap.getGreenfootTime()) {
			getWorld().removeObject(this);
		}
	}

	public Point2D getCopyOfOffset() {
		return new Point2D.Double(bulletOffsetFromPlayer.getX(), bulletOffsetFromPlayer.getY());
	}

	/**
	 * @return Returns true if we hit a player/enemy
	 */
	public boolean handleCollision() {
		List<?> intersectingObjects = getIntersectingObjects(null);
		if (intersectingObjects.size() != 0) {
			for (Object o : intersectingObjects) {
				if ((typeToIgnore == EntityType.ENEMY && (o instanceof Enemy)) || (typeToIgnore == EntityType.PLAYER && (o instanceof Player)))
					continue;

				if (!(o instanceof IDamageable))
					continue;

				IDamageable id = (IDamageable) o;

				if (hitEntities.contains(id))
					continue;

				if (id.getHP() <= 0)
					continue;

				id.damage(damage);
				hitEntities.add(id);
				return true;
			}
			return false;
		}
		return false;
	}
}
