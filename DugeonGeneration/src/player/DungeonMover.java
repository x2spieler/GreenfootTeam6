package player;

import scrollWorld.ScrollActor;
import world.DungeonMap;

public class DungeonMover extends ScrollActor {

	private boolean noclip = false;
	private boolean sliding;

	/**
	 * @param sliding
	 *            If true the mover will slide when hitting a wall at an angle.
	 *            If false the mover will stop moving when it hits a wall.
	 */
	public DungeonMover(boolean sliding) {
		super();
		this.sliding = sliding;
	}

	/**
	 * Sliding defaults to true.
	 */
	public DungeonMover() {
		super();
		this.sliding = true;
	}

	private DungeonMap world = null;

	@Override
	public void setLocation(int x, int y) {
		DungeonMap world = getWorld();
		if (world != null && world.isLegalMove(this, x + world.getCameraX() - (world.getWidth() / 2),
				y + world.getCameraY() - (world.getHeight() / 2))) {
			super.setLocation(x, y);
		}
	}

	@Override
	public void move(int distance) {
		moveAtAngle(distance, getRotation());
	}

	/**
	 * Takes the angle to move in, so a call of moveAtAngle(10,
	 * dungeonMover.getRotation()) is equivalent to a call of move(10).
	 * 
	 * @param distance
	 *            in pixels
	 * @param angle
	 *            in degrees
	 */
	public void moveAtAngle(int distance, int angle) {
		DungeonMap world = getWorld();
		if (distance == 0 || world == null)
			return;
		angle = addAngles(0, angle);
		if (!noclip && ((distance > 0) ? isWallAhead(angle) : isWallAhead(addAngles(180, angle)))) {
			slidingMove(distance, angle);
		} else {
			shorteningMove(distance, angle);
		}
	}

	public static int addAngles(int angle1, int angle2) {
		return (angle1 + angle2) % 360;
	}

	private void slidingMove(int distance, int angle) {
		double radians = Math.toRadians(angle);
		double sin = Math.sin(radians);
		double cos = Math.cos(radians);
		int x = getGlobalX();
		int y = getGlobalY();
		int dx = (int) Math.round(cos * distance);
		int dy = (int) Math.round(sin * distance);
		boolean[] neighbours = getNeighbouringTilesAccessibility();
		if (distance > 0) {
			if ((!neighbours[0] && facingRight(angle)) || (!neighbours[2] && facingLeft(angle))) {
				dx = 0;
			}
			if ((!neighbours[1] && facingDown(angle)) || (!neighbours[3] && facingUp(angle))) {
				dy = 0;
			}
		} else {
			if ((!neighbours[0] && facingLeft(angle)) || (!neighbours[2] && facingRight(angle))) {
				dx = 0;
			}
			if ((!neighbours[1] && facingUp(angle)) || (!neighbours[3] && facingDown(angle))) {
				dy = 0;
			}
		}
		setGlobalLocation(x + dx, y + dy);
	}

	private boolean facingRight(int rotation) {
		if (rotation < 90 || rotation > 270) {
			return true;
		} else {
			return false;
		}
	}

	private boolean facingDown(int rotation) {
		if (rotation < 180 && rotation > 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean facingUp(int rotation) {
		if (rotation > 180) {
			return true;
		} else {
			return false;
		}
	}

	private boolean facingLeft(int rotation) {
		if (rotation < 270 && rotation > 90) {
			return true;
		} else {
			return false;
		}
	}

	private boolean[] getNeighbouringTilesAccessibility() {
		DungeonMap world = getWorld();
		boolean[] ret = new boolean[4];
		int x = getGlobalX();
		int y = getGlobalY();
		ret[0] = world.isInAccessibleTile(x + 1, y);
		ret[1] = world.isInAccessibleTile(x, y + 1);
		ret[2] = world.isInAccessibleTile(x - 1, y);
		ret[3] = world.isInAccessibleTile(x, y - 1);
		return ret;
	}

	private boolean isWallAhead(int angle) {
		DungeonMap world = getWorld();
		int direction = angle / 90;
		int x = getGlobalX();
		int y = getGlobalY();
		if (angle % 90 == 0) {
			return !world.isInAccessibleTile(x + ((direction % 2 == 0) ? ((direction == 0) ? 1 : -1) : 0),
					y + ((direction % 2 == 1) ? ((direction == 1) ? 1 : -1) : 0));
		} else {
			return !(world.isInAccessibleTile(x + ((direction % 3 == 0) ? 1 : -1), y)
					&& world.isInAccessibleTile(x, y + ((direction < 2) ? 1 : -1)));
		}
	}

	private void shorteningMove(int distance, int angle) {
		DungeonMap world = getWorld();
		double radians = Math.toRadians(angle);
		double sin = Math.sin(radians);
		double cos = Math.cos(radians);
		int x = getGlobalX();
		int y = getGlobalY();
		for (int i = distance; i != 0; i -= ((distance > 0) ? 1 : -1)) {
			int dx = (int) Math.round(cos * i);
			int dy = (int) Math.round(sin * i);
			if (world.isLegalMove(this, x + dx, y + dy) || noclip) {
				setGlobalLocation(x + dx, y + dy);
				return;
			}
		}
	}

	@Override
	public DungeonMap getWorld() {
		if (world == null) {
			world = (DungeonMap) super.getWorld();
		}
		if (world == null) {
			throw new IllegalStateException("World has not been set. Unable to perform that Action.");
		}
		return world;
	}

	public boolean isTouchingWall() {
		boolean[] neighbours = getNeighbouringTilesAccessibility();
		for (int i = 0; i < neighbours.length; i++) {
			if (neighbours[i]) {
				return true;
			}
		}
		return false;
	}

	public boolean noclip() {
		return noclip;
	}

	public void setNoclip(boolean noclip) {
		this.noclip = noclip;
	}

	public boolean sliding() {
		return sliding;
	}

	public void setSliding(boolean sliding) {
		this.sliding = sliding;
	}

	// @Override
	// protected void addedToWorld(World world) {
	// if (world == null)
	// throw new IllegalArgumentException("Are you fucking kidding me?");
	// super.addedToWorld(world);
	// if (world instanceof DungeonMap) {
	// this.world = (DungeonMap) world;
	// } else {
	// throw new IllegalArgumentException("DungeonMover must only be added to a
	// DungeonMap");
	// }
	// }

}
