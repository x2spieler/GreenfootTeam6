package player;

import greenfoot.World;
import scrollWorld.ScrollActor;
import world.DungeonMap;

public class DungeonMover extends ScrollActor {

	private boolean noclip = false;

	private DungeonMap world = null;

	@Override
	public void setLocation(int x, int y) {
		if (world != null
				&& world.isLegalMove(this, x + world.getCameraX() - (world.getWidth() / 2), y
						+ world.getCameraY() - (world.getHeight() / 2))) {
			super.setLocation(x, y);
		}
	}

	@Override
	public void move(int distance) {
		moveAtAngle(distance, 0);
	}

	/**
	 * Takes the angle relative to the direction the mover is facing, so a call
	 * of moveAtAngle(10,0) is equivalent to a call of move(10).
	 * 
	 * @param distance
	 * @param angle
	 *            the angle in degrees
	 */
	public void moveAtAngle(int distance, int angle) {
		if (distance == 0 || world == null)
			return;
		if ((distance > 0) ? isWallAhead(0 + angle) : isWallAhead(180 + angle)) {
			slidingMove(distance, angle);
		} else {
			shorteningMove(distance, angle);
		}
	}

	private int addAngles(int angle1, int angle2) {
		return (angle1 + angle2) % 360;
	}

	private void slidingMove(int distance, int angle) {
		int rotation = addAngles(getRotation(), angle);
		double radians = Math.toRadians(rotation);
		double sin = Math.sin(radians);
		double cos = Math.cos(radians);
		int x = getGlobalX();
		int y = getGlobalY();
		int dx = (int) Math.round(cos * distance);
		int dy = (int) Math.round(sin * distance);
		boolean[] neighbours = getNeighbouringTilesAccessibility();
		if (distance > 0) {
			if ((!neighbours[0] && facingRight(rotation))
					|| (!neighbours[2] && facingLeft(rotation))) {
				dx = 0;
			}
			if ((!neighbours[1] && facingDown(rotation)) || (!neighbours[3] && facingUp(rotation))) {
				dy = 0;
			}
		} else {
			if ((!neighbours[0] && facingLeft(rotation))
					|| (!neighbours[2] && facingRight(rotation))) {
				dx = 0;
			}
			if ((!neighbours[1] && facingUp(rotation)) || (!neighbours[3] && facingDown(rotation))) {
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
		int rotation = addAngles(getRotation(), angle);
		int direction = rotation / 90;
		int x = getGlobalX();
		int y = getGlobalY();
		if (rotation % 90 == 0) {
			return !world.isInAccessibleTile(x
					+ ((direction % 2 == 0) ? ((direction == 0) ? 1 : -1) : 0), y
					+ ((direction % 2 == 1) ? ((direction == 1) ? 1 : -1) : 0));
		} else {
			return !(world.isInAccessibleTile(x + ((direction % 3 == 0) ? 1 : -1), y) && world
					.isInAccessibleTile(x, y + ((direction < 2) ? 1 : -1)));
		}
	}

	private void shorteningMove(int distance, int angle) {
		int rotation = addAngles(getRotation(), angle);
		double radians = Math.toRadians(rotation);
		double sin = Math.sin(radians);
		double cos = Math.cos(radians);
		int x = getGlobalX();
		int y = getGlobalY();
		for (int i = distance; i != 0; i -= ((distance > 0) ? 1 : -1)) {
			int dx = (int) Math.round(cos * i);
			int dy = (int) Math.round(sin * i);
			if (world.isLegalMove(this, x + dx, y + dy)) {
				setGlobalLocation(x + dx, y + dy);
				return;
			}
		}
	}

	@Override
	public DungeonMap getWorld() {
		return world;
	}

	public boolean noclip() {
		return noclip;
	}

	public void setNoclip(boolean noclip) {
		this.noclip = noclip;
	}

	@Override
	protected void addedToWorld(World world) {
		if (world == null)
			throw new IllegalArgumentException("Are you fucking kidding me?");
		super.addedToWorld(world);
		if (world instanceof DungeonMap) {
			this.world = (DungeonMap) world;
		} else {
			throw new IllegalArgumentException("DungeonMover must only be added to a DungeonMap");
		}
	}

}
