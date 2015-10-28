package player;

import greenfoot.Greenfoot;
import greenfoot.World;
import scrollWorld.ScrollActor;
import AI.IWorldInterfaceForAI;

public class TileMover extends ScrollActor {

	private final int tileSize;
	private Direction direction;
	private boolean directionChanged = false;
	private int lastMove = 0;

	public TileMover(int tileSize) {
		super();
		this.tileSize = tileSize;
	}

	public void moveTiled(Direction dir, int move) {
		if (move > 0) {
			lastMove = move;
			if (dir != direction) {
				directionChanged = true;
			}
			if (isInTile() || direction == dir) {
				direction = dir;
				moveTiledUnconditionally(dir, move);
			}
		}
	}

	private void moveTiledUnconditionally(Direction dir, int move) {
		if (dir != null) {
			switch (dir) {
			case UP:
				setGlobalLocation(getGlobalX(), getGlobalY() - move);
				break;
			case LEFT:
				setGlobalLocation(getGlobalX() - move, getGlobalY());
				break;
			case DOWN:
				setGlobalLocation(getGlobalX(), getGlobalY() + move);
				break;
			case RIGHT:
				setGlobalLocation(getGlobalX() + move, getGlobalY());
				break;
			}
		}
	}

	@Override
	public void act() {
		super.act();
		if (!isInTile()) {
			if (direction == null) {
				setGlobalLocation(getGlobalX()
						+ (tileSize - (getGlobalX() % tileSize)), getGlobalY()
						+ (tileSize - (getGlobalY() % tileSize)));

			}
			if (directionChanged) {
				int move = lastMove;
				if (direction == Direction.UP || direction == Direction.DOWN) {
					move = Math.min(move, tileSize - (getGlobalY() % tileSize));
				} else {
					move = Math.min(move, tileSize - (getGlobalX() % tileSize));
				}
				moveTiledUnconditionally(direction, move);
				if (isInTile()) {
					directionChanged = false;
				}
			}
		}
	}

	private boolean isKeyboardInput() {
		return Greenfoot.isKeyDown(Direction.UP.key)
				|| Greenfoot.isKeyDown(Direction.LEFT.key)
				|| Greenfoot.isKeyDown(Direction.DOWN.key)
				|| Greenfoot.isKeyDown(Direction.RIGHT.key);
	}

	private boolean isKeyboardInput(Direction dir) {
		return Greenfoot.isKeyDown(dir.key);
	}

	private boolean isInTile() {
		if (getGlobalX() % tileSize == 0 && getGlobalY() % tileSize == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void addedToWorld(World world) {
		super.addedToWorld(world);
		if (!(world instanceof IWorldInterfaceForAI)) {
			throw new IllegalArgumentException(
					"TileMover must be added to a world implementing IWorldInterfaceForAI");
		} else if (((IWorldInterfaceForAI) world).getTileSize() != tileSize) {
			throw new IllegalArgumentException(
					"Tilesize of mover differs from tilesize of world");
		}
	}

}
