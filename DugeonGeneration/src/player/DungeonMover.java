package player;

import scrollWorld.ScrollActor;
import world.DungeonMap;
import world.Direction;

public class DungeonMover extends ScrollActor {

	private boolean noclip = false;
	private boolean sliding;
	private int extent[];

	/**
	 * @param sliding
	 *            If true the mover will slide when hitting a wall at an angle.
	 *            If false the mover will stop moving when it hits a wall.
	 * @param extent
	 *            takes 1,2 or 4 arguments. 1 argument means the same extent in
	 *            all four directions, 2 arguments describe a rectangle centered
	 *            on the mover and 4 arguments gives the extent in each
	 *            direction in a clockwise manner, starting with the right.
	 * 
	 */
	public DungeonMover(boolean sliding, int... extent) {
		super();
		this.sliding = sliding;
		if (extent.length == 3 || extent.length > 4)
			throw new IllegalArgumentException("extent may contain either 1, 2 or 4 elements");
		if (extent.length == 0)
			this.extent = new int[] { -1 };
		else
			this.extent = extent;
	}

	/**
	 * Sliding defaults to true.
	 */
	public DungeonMover() {
		super();
		this.sliding = true;
		this.extent = new int[] { -1 };
	}

	private DungeonMap world = null;

	@Override
	public void setLocation(int x, int y) {
		DungeonMap world = getWorld();
		if (world != null && world.isLegalMove(this, x + world.getCameraX() - (world.getWidth() / 2), y + world.getCameraY() - (world.getHeight() / 2))) {
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
		return rotation < 90 || rotation > 270;
	}

	private boolean facingDown(int rotation) {
		return rotation < 180 && rotation > 0;
	}

	private boolean facingUp(int rotation) {
		return rotation > 180;
	}

	private boolean facingLeft(int rotation) {
		return rotation < 270 && rotation > 90;
	}

	private boolean[] getNeighbouringTilesAccessibility() {
		DungeonMap world = getWorld();
		boolean[] ret = new boolean[4];
		int x = getGlobalX();
		int y = getGlobalY();
		ret[0] = !world.hasCollision(x + 1, y, this);
		ret[1] = !world.hasCollision(x, y + 1, this);
		ret[2] = !world.hasCollision(x - 1, y, this);
		ret[3] = !world.hasCollision(x, y - 1, this);
		return ret;
	}

	private boolean isWallAhead(int angle) {
		DungeonMap world = getWorld();
		int direction = angle / 90;
		int x = getGlobalX();
		int y = getGlobalY();
		if (angle % 90 == 0) {
			return world.hasCollision(x + ((direction % 2 == 0) ? ((direction == 0) ? 1 : -1) : 0), y + ((direction % 2 == 1) ? ((direction == 1) ? 1 : -1) : 0), this);
		} else {
			boolean wallVertical = world.hasCollision(x + ((direction % 3 == 0) ? 1 : -1), y, this);
			boolean wallHorizontal = world.hasCollision(x, y + ((direction < 2) ? 1 : -1), this);
			return wallVertical || wallHorizontal && !(wallHorizontal && wallVertical);
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
			if (world.isLegalMove(this, x + dx, y + dy)) {
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
			if (!neighbours[i]) {
				return true;
			}
		}
		return false;
	}

	public int getExtentIn(final Direction direction) {
		if (extent.length == 1)
			return (extent[0] > 0) ? extent[0] : 0;
		final boolean centered = extent.length == 2;
		switch (direction) {
		case DOWN:
			return extent[1];
		case LEFT:
			return extent[(centered) ? 0 : 2];
		case RIGHT:
			return extent[0];
		case UP:
			return extent[(centered) ? 1 : 3];
		default:
			throw new IllegalArgumentException("unknown direction: " + direction);
		}
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

	public int[] getExtent() {
		return extent;
	}

}
